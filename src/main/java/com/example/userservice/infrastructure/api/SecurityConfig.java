package com.example.userservice.infrastructure.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/actuator/health").permitAll()
            .anyRequest().authenticated())
        .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt
            .jwtAuthenticationConverter(jwtAuthenticationConverter())));
    return http.build();
  }

  private JwtAuthenticationConverter jwtAuthenticationConverter() {
    JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();

    jwtConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
      Collection<GrantedAuthority> authorities = new ArrayList<>();

      Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
      if (resourceAccess != null) {
        Object clientAccessObj = resourceAccess.get("microservices");
        if (clientAccessObj instanceof Map<?, ?> clientAccessMap) {
          @SuppressWarnings("unchecked")
          Map<String, Object> clientAccess = (Map<String, Object>) clientAccessMap;
          Object rolesObj = clientAccess.get("roles");
          if (rolesObj instanceof List<?> rolesList) {
            for (Object roleObj : rolesList) {
              if (roleObj instanceof String role) {
                authorities.add(new SimpleGrantedAuthority(role));
              }
            }
          }
        }
      }

      return authorities;
    });

    return jwtConverter;
  }
}

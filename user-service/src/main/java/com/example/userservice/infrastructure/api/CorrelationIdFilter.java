package com.example.userservice.infrastructure.api;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class CorrelationIdFilter extends OncePerRequestFilter {

  private static final String CORRELATION_ID_HEADER = "X-Correlation-ID";

  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain)
      throws ServletException, IOException {

    try {
      String correlationId = request.getHeader(CORRELATION_ID_HEADER);

      if (correlationId == null || correlationId.isBlank()) {
        correlationId = UUID.randomUUID().toString();
      }

      // Poner en el contexto de log (MDC)
      MDC.put(CORRELATION_ID_HEADER, correlationId);

      // También podrías devolverlo en la respuesta
      response.setHeader(CORRELATION_ID_HEADER, correlationId);

      filterChain.doFilter(request, response);
    } finally {
      MDC.remove(CORRELATION_ID_HEADER);
    }
  }
}

package com.example.notification.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserCreatedEvent {
    private String email;
    private String name;

    public UserCreatedEvent() {}
    public UserCreatedEvent(String email, String name) {
        this.email = email;
        this.name = name;
    }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}

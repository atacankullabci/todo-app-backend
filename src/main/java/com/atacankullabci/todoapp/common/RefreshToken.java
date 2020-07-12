package com.atacankullabci.todoapp.common;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document
public class RefreshToken {

    @Id
    private String id;
    private String token;
    private Instant createdAt;

    public RefreshToken() {
    }

    public RefreshToken(String id, String token, Instant createdAt) {
        this.id = id;
        this.token = token;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}

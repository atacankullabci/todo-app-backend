package com.atacankullabci.todoapp.common;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document
public class VerificationToken {
    @Id
    private String id;
    private String token;
    @DBRef
    private User user;
    private Instant expirationDate;

    public VerificationToken() {
    }

    public VerificationToken(String id, String token, User user, Instant expirationDate) {
        this.id = id;
        this.token = token;
        this.user = user;
        this.expirationDate = expirationDate;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Instant getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Instant expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public String toString() {
        return "VerificationToken{" +
                "id='" + id + '\'' +
                ", token='" + token + '\'' +
                ", user=" + user +
                ", expirationDate=" + expirationDate +
                '}';
    }
}

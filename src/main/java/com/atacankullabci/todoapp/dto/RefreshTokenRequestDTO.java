package com.atacankullabci.todoapp.dto;

import javax.validation.constraints.NotBlank;

public class RefreshTokenRequestDTO {

    @NotBlank
    private String refreshToken;
    private String username;

    public RefreshTokenRequestDTO() {
    }

    public RefreshTokenRequestDTO(@NotBlank String refreshToken, String username) {
        this.refreshToken = refreshToken;
        this.username = username;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

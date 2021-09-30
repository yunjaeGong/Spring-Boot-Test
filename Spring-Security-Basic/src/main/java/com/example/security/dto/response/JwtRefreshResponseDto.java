package com.example.security.dto.response;

import lombok.Getter;

@Getter
public class JwtRefreshResponseDto {
    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";

    public JwtRefreshResponseDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}

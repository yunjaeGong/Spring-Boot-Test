package com.example.security.dto.response;

import com.example.security.jwt.JwtProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class JwtResponseDto {
    private String token;
    private String type = "JWT";
    private String refreshToken;
    private int id;
    private String username;
    private String email;
    private List<String> roles;

    public JwtResponseDto(String accessToken, String refreshToken, int id, String username, String email, List<String> roles) {
        this.token = accessToken;
        this.refreshToken = refreshToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }
}

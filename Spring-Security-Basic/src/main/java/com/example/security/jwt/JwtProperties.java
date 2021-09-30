package com.example.security.jwt;

public interface JwtProperties {
    String SECRET = "test"; // server의 private key
    int EXPIRATION_TIME = 60*1000; // 1/1000초, 1분
    int REFRESH_EXPIRATION_TIME = 5*60*1000; // 1/1000초, 5분
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
}

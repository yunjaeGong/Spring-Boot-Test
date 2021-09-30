package com.example.security.exception;

public class TokenRefreshException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public TokenRefreshException(String refreshToken, String message) {
        super(String.format("Failed for [%s]: %s", refreshToken, message));

    }
}

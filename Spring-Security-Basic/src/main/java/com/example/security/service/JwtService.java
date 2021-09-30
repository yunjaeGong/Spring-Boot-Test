package com.example.security.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.security.exception.RefreshTokenExpiredException;
import com.example.security.jwt.JwtProperties;
import com.example.security.model.RefreshToken;
import com.example.security.model.User;
import com.example.security.repository.RefreshTokenRepository;
import com.example.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static com.example.security.jwt.JwtProperties.REFRESH_EXPIRATION_TIME;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public String generateToken(User user) {
        return JWT.create()
                .withSubject("testToken")
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
                .withClaim("id", user.getId())
                .withClaim("username", user.getUsername())
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));
    }

    public RefreshToken generateRefreshToken(User user) {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(userRepository.findById(user.getId()).get());
        refreshToken.setExpiryDate(Instant.now().plusMillis(REFRESH_EXPIRATION_TIME));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }
//kevin gong gay
    public RefreshToken verifyRefreshTokenExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RefreshTokenExpiredException(token.getToken(), "Refresh token was expired. Please make a new signin request");
        } // GlobalException Handler가 Exception catch해 alert, redirect script 실행

        return token;
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

}

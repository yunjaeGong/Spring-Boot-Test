package com.example.security.repository;

import com.example.security.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    Optional<RefreshToken> findById(int id);
    Optional<RefreshToken> findByToken(String token);
    void deleteByToken(String token);
}

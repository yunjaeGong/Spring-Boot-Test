package com.example.security.controller;

import com.example.security.config.auth.PrincipalDetails;
import com.example.security.dto.request.TokenRefreshRequestDto;
import com.example.security.dto.response.JwtRefreshResponseDto;
import com.example.security.exception.TokenRefreshException;
import com.example.security.jwt.JwtUtils;
import com.example.security.model.RefreshToken;
import com.example.security.repository.RefreshTokenRepository;
import com.example.security.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class RestApiController {

    final JwtUtils jwtUtils;

    final JwtService jwtService;

    final RefreshTokenRepository refreshTokenRepository;

    @GetMapping("/home")
    public String home() {
        return "<h1>home</h1>";
    }

    @PostMapping("/api/token")
    public String token() {
        return "<h1>token</h1>";
    }

    @GetMapping("/api/user")
    public String user(Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("authentication: " + principalDetails.getUsername());
        return "<h1>user</h1>";
    }

    @GetMapping("/api/manager")
    public String manager() {
        return "manager";
    }

    @GetMapping("/api/admin")
    public String admin() {
        return "admin";
    }

    @PostMapping("/api/refreshToken")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequestDto requestRefreshToken, BindingResult bindingResult) {
        String refreshToken = requestRefreshToken.getRefreshToken();

        // 새로운 accessToken 생성
        return jwtService.findByToken(refreshToken)
                .map(jwtService::verifyRefreshTokenExpiration)
                .map(RefreshToken::getUser)
                .map(user-> {
                    String accessToken = jwtUtils.generateJwtTokenFromUser(user);
                    return ResponseEntity.ok(new JwtRefreshResponseDto(accessToken, refreshToken));
                    // TODO: refreshToken 삭제하기
                }).orElseThrow(() -> {
                    refreshTokenRepository.deleteByToken(refreshToken);
                    return new TokenRefreshException(refreshToken, "Refresh token is not in database!");
                });
    }
}

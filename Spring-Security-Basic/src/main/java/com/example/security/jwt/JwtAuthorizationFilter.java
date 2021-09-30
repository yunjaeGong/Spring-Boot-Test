package com.example.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.security.config.auth.PrincipalDetails;
import com.example.security.model.User;
import com.example.security.repository.UserRepository;
import com.example.security.service.JwtService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.example.security.jwt.JwtProperties.*;

// Security filter chain 중 BasicAuthenticationFilter존재
// 권한이나 인증이 필요한 특정 주소를 요청했을 때 위 필터를 무조건 거침
// 만약 권한이나 인증이 필요한 주소가 아니라면 이 필터 거치지 않음.

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private JwtUtils jwtUtils;

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("인증이나 권한이 필요한 주소 요청됨");

        // Header 정보, validate
        String jwtHeader = request.getHeader(HEADER_STRING);
        System.out.println("jwtHeader: " + jwtHeader);

        if (jwtHeader == null || !jwtHeader.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        String jwtToken = jwtHeader.replace(TOKEN_PREFIX, "");

        // User 정보 확인, SecurityContext에 추가
        String username = JwtUtils.getUsernameFromJwtToken(jwtToken);

        if (username != null) {
            User userEntity = userRepository.findByUsername(username);
            if(userEntity == null) {
                chain.doFilter(request, response);
                return;
            }

            // jwt토큰 서명만을 통해 만든 객체. 서명이 정상이면 객체 생성
            PrincipalDetails principalDetails = new PrincipalDetails(userEntity);
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
            // 인증된 사용자이므로 강제로 authentication 객체 생성 문제 없음

            SecurityContextHolder.getContext().setAuthentication(authentication);
            // 강제로 시큐리티 세션에 접근해 authentication 객체 저장.

        }
        chain.doFilter(request, response);
    }
}

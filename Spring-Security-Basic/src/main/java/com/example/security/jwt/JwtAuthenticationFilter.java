package com.example.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.security.config.auth.PrincipalDetails;
import com.example.security.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;

// Spring Security에 UsernamePasswordAuthenticationFilter존재
// @Order(2)에서 /login 요청 시 username, password 전송하면 UsernamePasswordAuthenticationFilter가 자동으로 동작
// @Order(1) entry point에서는 이 필터가 동작(formLogin 허용 x)
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final int EXPIRATION = 60000;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        this.setFilterProcessesUrl("/api/login");
    }

    // /api/** 에 대해 이 필터가 동작하게 하기 위해서는 이 필터를 직접 등록 필요
    // 로그인 처리 위해 setFilterProcessesUrl(String filterProcessesUrl)

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("JwtAuthenticationFilter: 로그인 시도");

        ObjectMapper om = new ObjectMapper();

        // 2. 정상인지 로그인 시. authenticationManager로 로그인을 시도하면
        // PrincipalDetailsService 호출, loadUserByUsername() 호출됨
        try {
            User user = om.readValue(request.getInputStream(), User.class);
            // username, password로 토큰 생성
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

            // PrincipalDetailsService의 loadUserByUsername가 실행된 후 정상이 authentication이 반환됨
            Authentication authentication =
                    authenticationManager.authenticate(authenticationToken);
            // authentication에는 db에 있는 로그인 정보 담김.


            // PrincipalDetails principal = (PrincipalDetails)authentication.getPrincipal();

            // 3. PrincipalDetails를 세션에 담고(authentication overriding) -> 권한 관리 위해
            // authentication 객체는 session 영역에 저장됨 => 로그인 완료
            return authentication;
        } catch (IOException e) {
            e.printStackTrace();
        }

        // return super.attemptAuthentication(request, response);
        return null;
    }

    // attemptAuthentication에서 로그인 시도 후 성공하면 successfulAuthentication 실행됨
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        // 4. JWT 토근을 담아 응답
        System.out.println("successfulAuthentication: 인증 완료");
        PrincipalDetails principalDetails = (PrincipalDetails)authResult.getPrincipal();

        String jwtToken = JWT.create()
                .withSubject("testToken")
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION))
                .withClaim("id", principalDetails.getUser().getId())
                .withClaim("username", principalDetails.getUser().getUsername())
                .sign(Algorithm.HMAC512("test"));

        response.addHeader("Authorization", "Bearer " + jwtToken);
        // jwt 토큰 포함한 헤더 사용자에게
        super.successfulAuthentication(request, response, chain, authResult);
    }
}

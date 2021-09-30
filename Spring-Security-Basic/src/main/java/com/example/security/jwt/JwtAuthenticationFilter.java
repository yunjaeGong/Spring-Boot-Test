package com.example.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.security.config.auth.PrincipalDetails;
import com.example.security.dto.response.JwtResponseDto;
import com.example.security.model.RefreshToken;
import com.example.security.model.User;
import com.example.security.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

// Spring Security에 UsernamePasswordAuthenticationFilter존재
// @Order(2)에서 /login 요청 시 username, password 전송하면 UsernamePasswordAuthenticationFilter가 자동으로 동작
// @Order(1) entry point에서는 이 필터가 동작(formLogin 허용 x)
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
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
        ObjectMapper om = new ObjectMapper();

        // 4. JWT 토근을 담아 응답
        System.out.println("successfulAuthentication: 인증 완료");
        PrincipalDetails principalDetails = (PrincipalDetails)authResult.getPrincipal();

        // accessToken 생성
        String jwtToken = jwtService.generateToken(principalDetails.getUser());

        // refreshToken 생성
        RefreshToken refreshToken = jwtService.generateRefreshToken(principalDetails.getUser());


        List<String> roles = principalDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());


        // jwt 토큰 포함한 헤더 사용자에게
        // response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.EXPIRATION_TIME + jwtToken);

        // token 포함한 response 객체
        String responseJson = om.writeValueAsString(new JwtResponseDto(jwtToken, refreshToken.getToken(), principalDetails.getUser().getId(),
                principalDetails.getUsername(), principalDetails.getUser().getEmail(), roles));

        // response 객체에 JwtResponseDto Json 전달
        response.getWriter().write(responseJson);
    }
}

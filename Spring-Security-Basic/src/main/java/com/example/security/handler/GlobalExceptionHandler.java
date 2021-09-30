package com.example.security.handler;

import com.example.security.exception.RefreshTokenExpiredException;
import com.example.security.exception.TokenRefreshException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
public class GlobalExceptionHandler {

    /* TODO: TokenExpiredException 발생 시 처리
    * 1. 유효하면 accessToken 재발급 후 body로 반환
    * 2. 유효하지 않으면 RefreshTokenExpiredException 발생
    */

    @ExceptionHandler(value = RefreshTokenExpiredException.class) // IllegalArgumentException이 발생하면 Spring이 아래 메소드로 Exception 전달
    public String handleRefreshTokenExpiredApiException(RefreshTokenExpiredException e) {
        System.out.println("RefreshTokenExpiredException");
        // 오류를 alert로 보이고, login 페이지로 redirect
        // client(browser) 응답에는 script가 좋음
        // ajax, android(개발자) 통신에는 DTO 처리가 편리.

        StringBuffer sb = new StringBuffer();
        sb.append("<script>");
        sb.append("alert('").append(e.getMessage()).append("')");
        sb.append("window.location.replace(\"localhost:8081/loginForm\");");
        sb.append("</script>");

        return sb.toString();
    };

    @ExceptionHandler(value = TokenRefreshException.class)
    // @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<?> handleTokenRefreshException(TokenRefreshException e) {
        // AccessToken refresh 중 에러 발생
        // e.g. refresh token 존재x
        return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
    }

}

package com.example.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.security.config.auth.PrincipalDetails;
import com.example.security.model.User;
import org.springframework.stereotype.Component;

import java.security.SignatureException;
import java.util.Date;

import static com.example.security.jwt.JwtProperties.*;

@Component
public class JwtUtils {

    public String generateJwtTokenFromPrincipal(PrincipalDetails principalDetails) {
        return generateJwtTokenFromUser(principalDetails.getUser());
    }

    public String generateJwtTokenFromUser(User user) {

        return JWT.create()
                .withSubject("testToken")
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
                .withClaim("id", user.getId())
                .withClaim("username", user.getUsername())
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));
    }

    public static String getUsernameFromJwtToken(String jwtToken) {
        String username = "";
        try {
            username = JWT.require(Algorithm.HMAC512(SECRET)).build().verify(jwtToken).getClaim("username").asString();
        } catch (JWTDecodeException e) {
            System.out.println("Invalid JWT token: " + e.getMessage());
        } catch (TokenExpiredException e) {
            System.out.println("JWT token had expired " + e.getMessage());
        } catch (JWTVerificationException e) {
            System.out.println("Cannot verify JWT token: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("JWT claims string is empty: " + e.getMessage());
        }
        return username;
    }


}

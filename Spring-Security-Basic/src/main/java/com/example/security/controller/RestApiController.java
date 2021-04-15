package com.example.security.controller;

import com.example.security.config.auth.PrincipalDetails;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
public class RestApiController {

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
}

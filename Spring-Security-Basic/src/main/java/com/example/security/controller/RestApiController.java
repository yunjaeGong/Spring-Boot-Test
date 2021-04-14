package com.example.security.controller;

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
}

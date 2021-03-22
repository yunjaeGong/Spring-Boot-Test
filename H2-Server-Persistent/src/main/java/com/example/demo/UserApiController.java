package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.net.http.HttpResponse;
import java.util.List;

@RestController
public class UserApiController {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @GetMapping("/api/getTest")
    public List<User> getUsers() {
        return userRepository.findAll();
    }
}

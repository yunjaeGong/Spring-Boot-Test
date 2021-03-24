package com.example.demo.controller.api;

import com.example.demo.repository.UserRepository;
import com.example.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserApiController {

    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    @GetMapping("/api/getUsers")
    public List<User> getUsers() {
        return userRepository.findAll();
    }
}

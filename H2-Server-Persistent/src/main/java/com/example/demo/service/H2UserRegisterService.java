package com.example.demo.service;

import com.example.demo.repository.UserRepository;
import com.example.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;
@Service
public class H2UserRegisterService {

    @Autowired
    private UserRepository userRepository;


    public User randUser(){
        /*
         * 랜덤유저생성
         * @return 생성된 유저
         *
         * https://jehuipark.github.io/java/springboot-h2-tcp-setup
         */
        User rand = User.builder()
                .userId(System.currentTimeMillis() + "")
                .age(new Random().nextInt(31)+1)
                .username("홍길동")
                .build();
        return userRepository.save(rand);
    }
}

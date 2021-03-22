package com.example.demo;

import lombok.RequiredArgsConstructor;
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
                .userName(new String[]{"김철수", "김영희", "짱구", "맹구", "슛돌이", "도꺠비", "김삿갓"}[new Random().nextInt(7)])
                .build();
        return userRepository.save(rand);
    }
}

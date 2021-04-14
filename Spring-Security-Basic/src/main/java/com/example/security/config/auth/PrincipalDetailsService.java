package com.example.security.config.auth;

import com.example.security.model.User;
import com.example.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
//
// loginProcessingUrl("/login") /login으로 요청이 오면 자동으로 loadUserByUsername 함수가 실행 @Order(2)
@Service
public class PrincipalDetailsService implements UserDetailsService {
    // Authentication 객체
    // PrincipalDetails는 프로그래머가 생성

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // form input의 name이 username이 아니면 parameter username에 주입 안됨
        // 만약 바꾸고 싶으면 config에서 formd의 usernameParameter 변경 필요
        User userEntity = userRepository.findByUsername(username);
        if (userEntity != null) {
            // System.out.println("loadUserByUsername: " + userEntity);
            return new PrincipalDetails(userEntity); // 반환값은 security session으로 <=(Authentication <= UserDetails)
        }
        return null;
    }
}

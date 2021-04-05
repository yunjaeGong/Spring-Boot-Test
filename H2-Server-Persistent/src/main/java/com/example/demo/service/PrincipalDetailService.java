package com.example.demo.service;

import com.example.demo.configuration.auth.PrincipalDetail;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PrincipalDetailService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User principal = userRepository.findByUserId(username);

        if(principal == null)
            throw new UsernameNotFoundException("해당 사용자는 찾을 수 없습니다 : " + username);

        System.out.println("loadUserByUsername: " + principal.getUserId());
        return new PrincipalDetail(principal);
    }
}

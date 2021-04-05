package com.example.demo.controller.api;

import com.example.demo.ResponseDto;
import com.example.demo.dto.NestedReplyPrincipleDto;
import com.example.demo.repository.UserRepository;
import com.example.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpResponse;
import java.util.List;

@RestController
public class UserApiController {

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder encoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Transactional(readOnly = true)
    @GetMapping("/api/getUsers")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Transactional
    @PostMapping("/api/user/nestedReplyLogin")
    public ResponseDto<Integer> nestedReplyLogin(@RequestBody NestedReplyPrincipleDto principleDto) {
        // eot
        User user = userRepository.findByUserId(principleDto.getUserId());
        if(user == null) {
            user = new User();
            user.setUserId(principleDto.getUserId());
            user.setPassword(encoder.encode(principleDto.getPassword()));

            userRepository.save(user);

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );

            // Authentication 생성(변경된 DB값으로) 및 변경
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }
}

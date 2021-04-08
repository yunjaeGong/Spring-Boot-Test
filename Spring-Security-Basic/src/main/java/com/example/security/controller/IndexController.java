package com.example.security.controller;

import com.example.security.repository.UserRepository;
import com.example.security.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class IndexController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping({"", "/"})
    private String index() {
        // mustache 기본 폴더 src/main/resources
        // view resolver 설정 시 templates(prefix) + .mustache(suffix)
        // jsp에서 /WEB-INF/views + .jsp와 동일
        return "index";
    }

    @GetMapping("/user")
    private @ResponseBody String user() {
        return "user";
    }

    @GetMapping("/admin")
    private @ResponseBody String admin() {
        return "admin";
    }

    @GetMapping("/manager")
    private @ResponseBody String manager() {
        return "manager";
    }

    @GetMapping("/loginForm")
    private  String loginForm() {
        return "loginForm";
    }

    @PostMapping("/join")
    private String join(User user) {
        // System.out.println(user.getId());

        user.setRole("ROLE_USER");
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);

        return "redirect:/loginForm";
    }

    @GetMapping("/joinForm")
    private String joinForm() {
        return "joinForm";
    }

    // @Secured("ROLE_ADMIN") // getAuthority 함수률 자동호출해서 찾기음  그 함수에 리턴값을 이메일로 잡아주면되요
    // @PreAuthorize("hasRole('ROLE_ADMIN')") // 메소드 실행 직전에ㄴ
    @GetMapping("/info")
    private @ResponseBody String info() {
        return "userDetail";
    }

    // @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')") // 메소드 실행 직전에ㄴ
    @GetMapping("/data")
    private @ResponseBody String data() {
        return "<h1>데이터</h1>";
    }

}

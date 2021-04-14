package com.example.security.controller;

import com.example.security.repository.UserRepository;
import com.example.security.model.User;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;


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
        user.setPassword(encoder.encode(user.getPassword()));

        userRepository.save(user);

        return "redirect:/loginForm";
    }

    @GetMapping("/joinForm")
    private String joinForm() {
        return "joinForm";
    }

    // @Secured("ROLE_ADMIN") // getAuthority 함수를 자동 호출해서 반환된 값에 따라 판단
    // @PreAuthorize("hasRole('ROLE_ADMIN')") // 메소드 실행 직전에 확인
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

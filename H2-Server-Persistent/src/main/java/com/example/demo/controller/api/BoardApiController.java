package com.example.demo.controller.api;

import com.example.demo.ResponseDto;
import com.example.demo.dto.NestedReplySaveDto;
import com.example.demo.dto.ReplySaveRequestDto;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.BoardService;
import com.example.demo.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
public class BoardApiController {

    @Autowired
    BoardService boardService;

    @Autowired
    ReplyService replyService;

    @Autowired
    UserRepository userRepository;

    PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/api/board/reply") // 댓글 작성 Request 주소
    public ResponseDto<Integer> saveReply(@RequestBody ReplySaveRequestDto replySaveRequestDto) {
        replyService.saveReply(replySaveRequestDto);
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }

    @PostMapping("/api/board/nestedReply") // 댓글 작성 Request 주소
    public ResponseDto<Integer> insertReply(@RequestBody NestedReplySaveDto nestedReplySaveDto) {
        User user = userRepository.findByUserId(nestedReplySaveDto.getUserId());
        if(user == null) {
            user = new User();
            user.setUserId(nestedReplySaveDto.getUserId());
            user.setPassword(encoder.encode(nestedReplySaveDto.getPassword()));

            userRepository.save(user);
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUserId(), user.getPassword())
        );

        System.out.println(authentication.getName());

        // Authentication 생성(변경된 DB값으로) 및 변경
        SecurityContextHolder.getContext().setAuthentication(authentication);
        replyService.insertReply(nestedReplySaveDto);
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }

    @DeleteMapping("/api/board/{boardId}/reply/{replyId}")
    public ResponseDto<Integer> deleteReply(@PathVariable int replyId) {
        replyService.deleteReply(replyId);
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }
}


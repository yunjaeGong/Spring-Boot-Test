package com.example.demo.controller.api;

import com.example.demo.ResponseDto;
import com.example.demo.dto.ReplySaveRequestDto;
import com.example.demo.service.BoardService;
import com.example.demo.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class BoardApiController {

    @Autowired
    BoardService boardService;

    @Autowired
    ReplyService replyService;

    @PostMapping("/api/board/reply") // 댓글 작성 Request 주소
    public ResponseDto<Integer> saveReply(@RequestBody ReplySaveRequestDto replySaveRequestDto) {
        replyService.saveReply(replySaveRequestDto);
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }

    @PostMapping("/api/board/nestedReply") // 댓글 작성 Request 주소
    public ResponseDto<Integer> insertReply(@RequestBody ReplySaveRequestDto replySaveRequestDto) {
        replyService.insertReply(replySaveRequestDto);
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }

    @DeleteMapping("/api/board/{boardId}/reply/{replyId}")
    public ResponseDto<Integer> deleteReply(@PathVariable int replyId) {
        replyService.deleteReply(replyId);
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }
}


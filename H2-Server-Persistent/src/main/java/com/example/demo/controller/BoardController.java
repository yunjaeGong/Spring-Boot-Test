package com.example.demo.controller;

import com.example.demo.service.BoardService;
import com.example.demo.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@Controller
public class BoardController {

    @Autowired
    private BoardService boardService;

    @Autowired
    private ReplyService replyService;

    @GetMapping({"", "/"})
    public String index(Model model) {
        model.addAttribute("board", boardService.getPost(1));
        return "boardReplyTest";
    }

    @GetMapping("/replyTest1")
    // https://bbbootstrap.com/snippets/bootstrap-collapsible-like-comment-and-share-section-43201372
    public String replyTest1(Model model) {
        model.addAttribute("board", boardService.getPost(2));
        model.addAttribute("rootReplies", replyService.getRootReplies(2));
        model.addAttribute("nestedReplies", replyService.getNestedReplies(2));
        return "collapsibleComment";
    }


}

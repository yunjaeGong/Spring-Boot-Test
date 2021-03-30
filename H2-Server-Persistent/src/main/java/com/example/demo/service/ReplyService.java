package com.example.demo.service;

import com.example.demo.dto.NestedReplySaveDto;
import com.example.demo.dto.ReplySaveRequestDto;
import com.example.demo.model.Board;
import com.example.demo.model.Reply;
import com.example.demo.model.User;
import com.example.demo.repository.BoardRepository;
import com.example.demo.repository.NestedReplyRepository;
import com.example.demo.repository.UserRepository;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReplyService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private NestedReplyRepository replyRepository;

    @Transactional
    public void saveReply(ReplySaveRequestDto replySaveRequestDto) {
        // 본 댓글용
        User user = userRepository.findById(replySaveRequestDto.getUserId()).orElseThrow(() -> {
            return new IllegalIdentifierException("댓글 쓰기 실패: 유저 아이디를 찾을 수 없습니다.");
        });

        Board board = boardRepository.findById(replySaveRequestDto.getBoardId()).orElseThrow(() -> {
            return new IllegalIdentifierException("댓글 쓰기 실패: 게시글 아이디를 찾을 수 없습니다.");
        });

        Reply reply = Reply.builder()
                .user(user)
                .board(board)
                .content(replySaveRequestDto.getContent())
                .parentId(0)
                .depth(0)
                .rootId(0)
                .build();

        replyRepository.save(reply);
    }

    @Transactional
    public void insertReply(NestedReplySaveDto nestedReplySaveDto) {
        // 본 댓글용

        Board board = boardRepository.findById(nestedReplySaveDto.getBoardId()).orElseThrow(() -> {
            return new IllegalIdentifierException("댓글 쓰기 실패: 게시글 아이디를 찾을 수 없습니다.");
        });

        Reply parentReply = replyRepository.findById(nestedReplySaveDto.getParentId()).orElseThrow(() -> {
            return new IllegalIdentifierException("댓글 쓰기 실패: 원 댓글의 아이디를 찾을 수 없습니다.");
        });
        /*
         * root는 (존재하면) 그대로
         * parentId = id
         * 대댓글이면 order number는 +1
         * */

        User user = userRepository.findByUserId(nestedReplySaveDto.getUserId());
        if(user == null) {
            user = new User();
            user.setUserId(nestedReplySaveDto.getUserId()); // anonymous user
            userRepository.save(user);
        }

        Reply reply = Reply.builder()
                .user(user)
                .board(board)
                .content(nestedReplySaveDto.getContent())
                .parentId(0)
                .depth(0)
                .rootId(0)
                .build();
// TODO: deoth 0, rootid 0 수정
        replyRepository.save(reply);
    }

    @Transactional(readOnly = true)
    public List<Reply> getRootReplies(int boardId) {
        return replyRepository.findAllByBoardIdAndDepth(boardId, 0);
    }

    @Transactional(readOnly = true)
    public List<Reply> getNestedReplies(int boardId) {
        return replyRepository.findAllByBoardIdAndDepth(boardId, 1);
    }

    @Transactional
    public void deleteReply(int id) {
        replyRepository.deleteById(id);
    }
}

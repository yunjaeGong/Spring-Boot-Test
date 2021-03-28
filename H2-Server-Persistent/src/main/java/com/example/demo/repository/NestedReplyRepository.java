package com.example.demo.repository;

import com.example.demo.model.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NestedReplyRepository extends JpaRepository<Reply, Integer> {
    List<Reply> findAllByBoardId(int boardId);
    List<Reply> findAllByBoardIdAndDepth(int boardId, int depth);
    List<Reply> findAllByBoardIdAndParentId(int boardId, int parentId);
}

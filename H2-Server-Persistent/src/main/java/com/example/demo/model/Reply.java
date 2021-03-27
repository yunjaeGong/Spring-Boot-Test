package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int parentId;

    @Lob
    @Column(nullable = false)
    private String content;

    @ManyToOne // 여러 답글 > 게시글
    @JoinColumn(name = "boardId", referencedColumnName="id")
    private Board board;

    @ManyToOne // 여러 답글 > 유저
    @JoinColumn(name = "userId", referencedColumnName="id")
    private User user;

    private int depth; // 댓글 시작점 깊이

    private int rootId; // root번 댓글의 대댓글들

    @CreationTimestamp
    private Timestamp createDate;
}
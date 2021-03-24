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

    private int parent_id;

    @Lob
    @Column(nullable = false)
    private String content;

    @ManyToOne // 여러 답글 > 게시글
    @JoinColumn(name = "board_id", referencedColumnName="id")
    private Board board;

    @ManyToOne // 여러 답글 > 유저
    @JoinColumn(name = "user_id", referencedColumnName="id")
    private User user;

    private int depth;

    private int rank;

    @CreationTimestamp
    private Timestamp createDate;
}
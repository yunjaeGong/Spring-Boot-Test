package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NestedReplySaveDto {
    private String userId;
    private int boardId;
    private String content;
    private int parentId;
    private int rootId;
}

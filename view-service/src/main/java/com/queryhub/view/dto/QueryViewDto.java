package com.queryhub.view.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class QueryViewDto {
    private Long id;
    private String title;
    private String description;
    private String topic;
    private Long authorId;
    private String owner;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CommentResponse> comments;
    private long likeCount;
}


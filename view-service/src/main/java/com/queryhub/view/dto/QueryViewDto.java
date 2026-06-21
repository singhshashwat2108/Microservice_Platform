package com.queryhub.view.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
}

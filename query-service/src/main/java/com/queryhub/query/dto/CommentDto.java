package com.queryhub.query.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class CommentDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentRequest {
        @NotNull(message = "Query ID is required")
        private Long queryId;

        @NotBlank(message = "Comment content is required")
        private String content;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentResponse {
        private Long id;
        private String content;
        private LocalDateTime createdAt;
        private Long queryId;
        private Long userId;
        private String userName;
    }
}

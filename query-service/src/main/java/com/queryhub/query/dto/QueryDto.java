package com.queryhub.query.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

public class QueryDto {

    @Data
    public static class CategoryRequest {
        @NotBlank(message = "Category name is required")
        private String name;
    }

    @Data
    public static class CategoryResponse {
        private Long id;
        private String name;

        public CategoryResponse(Long id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    @Data
    public static class QueryRequest {
        private String title;

        @NotBlank(message = "Query description is required")
        @JsonAlias({"Query", "query", "description"})
        private String description;

        @NotBlank(message = "Topic is required")
        @JsonAlias({"Topic", "topic", "category"})
        private String category;
    }

    @Data
    public static class QueryResponse {
        private Long id;
        private String title;
        private String description;
        private String topic;
        private Long authorId;
        private String owner;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private List<CommentDto.CommentResponse> comments;
        private long likeCount;

        public QueryResponse(Long id, String title, String description, String topic,
                             Long authorId, String owner,
                             LocalDateTime createdAt, LocalDateTime updatedAt) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.topic = topic;
            this.authorId = authorId;
            this.owner = owner;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }

        public QueryResponse(Long id, String title, String description, String topic,
                             Long authorId, String owner,
                             LocalDateTime createdAt, LocalDateTime updatedAt,
                             List<CommentDto.CommentResponse> comments, long likeCount) {
            this(id, title, description, topic, authorId, owner, createdAt, updatedAt);
            this.comments = comments;
            this.likeCount = likeCount;
        }
    }

    @Data
    @lombok.NoArgsConstructor
    public static class PagedResponse<T> {
        private List<T> content;
        private int page;
        private int size;
        private long totalElements;
        private int totalPages;

        public PagedResponse(List<T> content, int page, int size, long totalElements, int totalPages) {
            this.content = content;
            this.page = page;
            this.size = size;
            this.totalElements = totalElements;
            this.totalPages = totalPages;
        }
    }
}

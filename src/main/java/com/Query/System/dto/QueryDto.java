package com.Query.System.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;

public class QueryDto {

    public static class CategoryRequest {
        @NotBlank(message = "Category name is required")
        private String name;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }
 
    public static class QueryRequest {
        @NotBlank(message = "Topic is required")
        private String Topic;        

        @NotBlank(message = "Query is required")
        private String Query;        

        public String getTopic() { return Topic; }
        public void setTopic(String topic) { Topic = topic; }

        public String getQuery() { return Query; }
        public void setQuery(String query) { Query = query; }
    }
 
    public static class QueryResponse {
        private Long id;
        private String topic;
        private String queryText;
        private String owner;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public QueryResponse(Long id, String topic, String queryText,
                             String owner, LocalDateTime createdAt, LocalDateTime updatedAt) {
            this.id        = id;
            this.topic     = topic;
            this.queryText = queryText;
            this.owner     = owner;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }

        public Long getId() { return id; }
        public String getTopic() { return topic; }
        public String getQueryText() { return queryText; }
        public String getOwner() { return owner; }
        public LocalDateTime getCreatedAt() { return createdAt; }
        public LocalDateTime getUpdatedAt() { return updatedAt; }
    }
 
    public static class CategoryResponse {
        private Long id;
        private String name;
        private LocalDateTime createdAt;

        public CategoryResponse(Long id, String name, LocalDateTime createdAt) {
            this.id        = id;
            this.name      = name;
            this.createdAt = createdAt;
        }

        public Long getId() { return id; }
        public String getName() { return name; }
        public LocalDateTime getCreatedAt() { return createdAt; }
    }
} 

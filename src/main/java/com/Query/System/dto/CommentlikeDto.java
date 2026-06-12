package com.Query.System.dto;


import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;

public class CommentlikeDto {

    public static class CommentRequest {

        @NotBlank(message = "Comment text is required")
        private String commentText;

        private String commenterName;

        public String getCommentText() { return commentText; }
        public void setCommentText(String commentText) { this.commentText = commentText; }

        public String getCommenterName() { return commenterName; }
        public void setCommenterName(String commenterName) { this.commenterName = commenterName; }
    }

 
    public static class CommentResponse {
        private Long id;
        private String commentText;
        private String commenterName;
        private Long queryId;
        private LocalDateTime createdAt;

        public CommentResponse(Long id, String commentText, String commenterName,
                               Long queryId, LocalDateTime createdAt) {
            this.id            = id;
            this.commentText   = commentText;
            this.commenterName = commenterName;
            this.queryId       = queryId;
            this.createdAt     = createdAt;
        }

        public Long getId() { return id; }
        public String getCommentText() { return commentText; }
        public String getCommenterName() { return commenterName; }
        public Long getQueryId() { return queryId; }
        public LocalDateTime getCreatedAt() { return createdAt; }
    }

    public static class LikeResponse {
        private Long queryId;
        private long totalLikes;
        private String message;

        public LikeResponse(Long queryId, long totalLikes, String message) {
            this.queryId    = queryId;
            this.totalLikes = totalLikes;
            this.message    = message;
        }

        public Long getQueryId() { return queryId; }
        public long getTotalLikes() { return totalLikes; }
        public String getMessage() { return message; }
    }
}
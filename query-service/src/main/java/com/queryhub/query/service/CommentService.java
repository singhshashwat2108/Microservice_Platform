package com.queryhub.query.service;

import com.queryhub.common.handler.ResourceNotFoundException;
import com.queryhub.query.dto.CommentDto;
import com.queryhub.query.entity.CommentEntity;
import com.queryhub.query.entity.QueryEntity;
import com.queryhub.query.repository.CommentRepository;
import com.queryhub.query.repository.QueryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final QueryRepository queryRepository;

    public CommentService(CommentRepository commentRepository, QueryRepository queryRepository) {
        this.commentRepository = commentRepository;
        this.queryRepository = queryRepository;
    }

    @Transactional
    public CommentDto.CommentResponse createComment(CommentDto.CommentRequest request, Long userId, String userName) {
        QueryEntity query = queryRepository.findById(request.getQueryId())
                .orElseThrow(() -> new ResourceNotFoundException("Query with id " + request.getQueryId() + " not found."));

        CommentEntity comment = CommentEntity.builder()
                .content(request.getContent().trim())
                .queryId(query.getId())
                .userId(userId)
                .userName(userName)
                .build();

        commentRepository.save(comment);
        return toResponse(comment);
    }

    public List<CommentDto.CommentResponse> getCommentsByQuery(Long queryId) {
        if (!queryRepository.existsById(queryId)) {
            throw new ResourceNotFoundException("Query with id " + queryId + " not found.");
        }
        return commentRepository.findByQueryId(queryId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteComment(Long commentId, Long currentUserId, String role) {
        CommentEntity comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment with id " + commentId + " not found."));

        if ("ADMIN".equals(role)) {
            commentRepository.delete(comment);
            return;
        }

        if (comment.getUserId().equals(currentUserId)) {
            commentRepository.delete(comment);
            return;
        }

        QueryEntity query = queryRepository.findById(comment.getQueryId())
                .orElseThrow(() -> new ResourceNotFoundException("Query with id " + comment.getQueryId() + " not found."));

        if (query.getAuthorId().equals(currentUserId)) {
            commentRepository.delete(comment);
            return;
        }

        throw new SecurityException("You are not authorized to delete this comment.");
    }

    private CommentDto.CommentResponse toResponse(CommentEntity comment) {
        return CommentDto.CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .queryId(comment.getQueryId())
                .userId(comment.getUserId())
                .userName(comment.getUserName())
                .build();
    }
}

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
    private final CacheInvalidationService cacheInvalidationService;

    public CommentService(CommentRepository commentRepository, QueryRepository queryRepository,
                          CacheInvalidationService cacheInvalidationService) {
        this.commentRepository = commentRepository;
        this.queryRepository = queryRepository;
        this.cacheInvalidationService = cacheInvalidationService;
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
        cacheInvalidationService.invalidate(query.getId(), query.getCategory().getName());
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

        QueryEntity query = queryRepository.findById(comment.getQueryId())
                .orElseThrow(() -> new ResourceNotFoundException("Query with id " + comment.getQueryId() + " not found."));

        if (!"ADMIN".equals(role) && !comment.getUserId().equals(currentUserId) && !query.getAuthorId().equals(currentUserId)) {
            throw new SecurityException("You are not authorized to delete this comment.");
        }

        commentRepository.delete(comment);
        cacheInvalidationService.invalidate(comment.getQueryId(), query.getCategory().getName());
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

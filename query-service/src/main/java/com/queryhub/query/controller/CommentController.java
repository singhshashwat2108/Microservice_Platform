package com.queryhub.query.controller;

import com.queryhub.common.security.JwtPrincipal;
import com.queryhub.query.dto.CommentDto;
import com.queryhub.query.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<CommentDto.CommentResponse> createComment(
            @Valid @RequestBody CommentDto.CommentRequest request,
            @AuthenticationPrincipal JwtPrincipal principal) {
        CommentDto.CommentResponse response = commentService.createComment(
                request, principal.getUserId(), principal.getUsername());
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/query/{queryId}")
    public ResponseEntity<List<CommentDto.CommentResponse>> getCommentsByQuery(
            @PathVariable Long queryId) {
        List<CommentDto.CommentResponse> response = commentService.getCommentsByQuery(queryId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteComment(
            @PathVariable Long id,
            @AuthenticationPrincipal JwtPrincipal principal) {
        commentService.deleteComment(id, principal.getUserId(), principal.getRole());
        return ResponseEntity.ok(Map.of("message", "Comment deleted successfully."));
    }
}

package com.Query.System.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Query.System.dto.CommentlikeDto;
import com.Query.System.services.Commentservice;
import com.Query.System.services.Likeservice;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/Query")
public class CommentLikeController {

    private final Commentservice commentService;
    private final Likeservice    likeService;

    public CommentLikeController(Commentservice commentService,
                                 Likeservice likeService) {
        this.commentService = commentService;
        this.likeService    = likeService;
    }

    // ─── POST /Query/{id}/comment ─────────────────────────
    // Add a comment — no login required
    // Body: { "commentText": "...", "commenterName": "..." (optional) }
    @PostMapping("/{id}/comment")
    public ResponseEntity<CommentlikeDto.CommentResponse> addComment(
            @PathVariable Long id,
            @Valid @RequestBody CommentlikeDto.CommentRequest request) {

        return ResponseEntity.status(201)
                .body(commentService.addComment(id, request));
    }

    @GetMapping("/{id}/comment")
    public ResponseEntity<List<CommentlikeDto.CommentResponse>> getComments(
            @PathVariable Long id) {

        return ResponseEntity.ok(commentService.getComments(id));
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<CommentlikeDto.LikeResponse> likeQuery(
            @PathVariable Long id) {

        return ResponseEntity.ok(likeService.likeQuery(id));
    }

    @GetMapping("/{id}/like")
    public ResponseEntity<CommentlikeDto.LikeResponse> getLikeCount(
            @PathVariable Long id) {

        return ResponseEntity.ok(likeService.getLikeCount(id));
    }
}
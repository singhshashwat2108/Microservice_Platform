package com.queryhub.query.controller;

import com.queryhub.common.security.JwtPrincipal;
import com.queryhub.query.service.LikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/likes")
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping("/{queryId}")
    public ResponseEntity<Map<String, String>> addLike(
            @PathVariable Long queryId,
            @AuthenticationPrincipal JwtPrincipal principal) {
        likeService.addLike(queryId, principal.getUserId(), principal.getUsername());
        return ResponseEntity.ok(Map.of("message", "Like added successfully."));
    }

    @DeleteMapping("/{queryId}")
    public ResponseEntity<Map<String, String>> removeLike(
            @PathVariable Long queryId,
            @AuthenticationPrincipal JwtPrincipal principal) {
        likeService.removeLike(queryId, principal.getUserId());
        return ResponseEntity.ok(Map.of("message", "Like removed successfully."));
    }

    @GetMapping("/{queryId}/count")
    public ResponseEntity<Map<String, Long>> getLikeCount(@PathVariable Long queryId) {
        long count = likeService.getLikeCount(queryId);
        return ResponseEntity.ok(Map.of("likeCount", count));
    }
}

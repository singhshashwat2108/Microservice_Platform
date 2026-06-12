package com.Query.System.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.Query.System.dto.CommentlikeDto;
import com.Query.System.entity.Comment;
import com.Query.System.entity.Query;
import com.Query.System.repository.CommentRepo;
import com.Query.System.repository.QueryRepo;

@Service
public class Commentservice{

    private final CommentRepo commentRepository;
    private final QueryRepo   queryRepository;

    public Commentservice(CommentRepo commentRepository,
                          QueryRepo queryRepository) {
        this.commentRepository = commentRepository;
        this.queryRepository   = queryRepository;
    }
 
    public CommentlikeDto.CommentResponse addComment(Long queryId,
                                                     CommentlikeDto.CommentRequest request) {
        Query query = findQueryOrThrow(queryId);


        String name = (request.getCommenterName() == null
                    || request.getCommenterName().isBlank())
                ? "Anonymous"
                : request.getCommenterName().trim();

        Comment comment = new Comment();
        comment.setCommentText(request.getCommentText().trim());
        comment.setCommenterName(name);
        comment.setQuery(query);
        commentRepository.save(comment);

        return toResponse(comment);
    }

    public List<CommentlikeDto.CommentResponse> getComments(Long queryId) {
        Query query = findQueryOrThrow(queryId);
        return commentRepository.findByQueryOrderByCreatedAtAsc(query)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ─── Helpers ──────────────────────────────────────────
    private Query findQueryOrThrow(Long queryId) {
        return queryRepository.findById(queryId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Query with id " + queryId + " not found."));
    }

    private CommentlikeDto.CommentResponse toResponse(Comment c) {
        return new CommentlikeDto.CommentResponse(
                c.getId(),
                c.getCommentText(),
                c.getCommenterName(),
                c.getQuery().getId(),
                c.getCreatedAt());
    }
}
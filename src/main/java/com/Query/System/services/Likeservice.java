package com.Query.System.services;


import org.springframework.stereotype.Service;

import com.Query.System.dto.CommentlikeDto;
import com.Query.System.entity.Like;
import com.Query.System.entity.Query;
import com.Query.System.repository.LikeRepo;
import com.Query.System.repository.QueryRepo;

@Service
public class Likeservice {

    private final LikeRepo likeRepository;
    private final QueryRepo queryRepository;

    public Likeservice(LikeRepo likeRepository,
                       QueryRepo queryRepository) {
        this.likeRepository  = likeRepository;
        this.queryRepository = queryRepository;
    }
 
    public CommentlikeDto.LikeResponse likeQuery(Long queryId) {
        Query query = findQueryOrThrow(queryId);

        Like like = new Like();
        like.setQuery(query);
        likeRepository.save(like);

        long total = likeRepository.countByQuery(query);
        return new CommentlikeDto.LikeResponse(queryId, total, "Liked!");
    }

    public CommentlikeDto.LikeResponse getLikeCount(Long queryId) {
        Query query = findQueryOrThrow(queryId);
        long total  = likeRepository.countByQuery(query);
        return new CommentlikeDto.LikeResponse(queryId, total, "");
    }

    private Query findQueryOrThrow(Long queryId) {
        return queryRepository.findById(queryId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Query with id " + queryId + " not found."));
    }
}
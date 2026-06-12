package com.Query.System.repository;

import com.Query.System.entity.Comment;
import com.Query.System.entity.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepo extends JpaRepository<Comment, Long> {

    // Get all comments for a specific query, ordered oldest first
    List<Comment> findByQueryOrderByCreatedAtAsc(Query query);

    // Count comments on a query
    long countByQuery(Query query);
}
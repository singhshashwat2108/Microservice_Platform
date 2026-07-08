package com.queryhub.query.repository;

import com.queryhub.query.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findByQueryId(Long queryId);
    void deleteByQueryId(Long queryId);
}

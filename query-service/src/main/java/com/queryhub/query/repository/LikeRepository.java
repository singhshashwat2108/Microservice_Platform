package com.queryhub.query.repository;

import com.queryhub.query.entity.LikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<LikeEntity, Long> {

    Optional<LikeEntity> findByQueryIdAndUserId(Long queryId, Long userId);

    long countByQueryId(Long queryId);

    void deleteByQueryId(Long queryId);
}

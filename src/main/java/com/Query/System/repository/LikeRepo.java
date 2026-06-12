package com.Query.System.repository;

import com.Query.System.entity.Like;
import com.Query.System.entity.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepo extends JpaRepository<Like, Long> {

    // Count total likes for a query
    long countByQuery(Query query);
}
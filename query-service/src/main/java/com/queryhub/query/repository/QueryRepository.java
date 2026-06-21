package com.queryhub.query.repository;

import com.queryhub.query.entity.Category;
import com.queryhub.query.entity.QueryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QueryRepository extends JpaRepository<QueryEntity, Long> {

    Page<QueryEntity> findByCategory(Category category, Pageable pageable);
}

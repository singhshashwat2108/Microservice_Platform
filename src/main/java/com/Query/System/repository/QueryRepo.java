package com.Query.System.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Query.System.entity.Category;
import com.Query.System.entity.Query;

@Repository
public interface QueryRepo extends JpaRepository<Query, Long> {

    
    List<Query> findByCategory(Category category);
 
    List<Query> findByOwner(String owner);
}
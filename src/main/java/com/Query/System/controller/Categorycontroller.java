package com.Query.System.controller;

import com.Query.System.dto.QueryDto;
import com.Query.System.services.Categoryservice;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class Categorycontroller {

    private final Categoryservice categoryService;

    public Categorycontroller(Categoryservice categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<QueryDto.CategoryResponse> createCategory(
            @Valid @RequestBody QueryDto.CategoryRequest request) {

        QueryDto.CategoryResponse response = categoryService.createCategory(request);
        return ResponseEntity.status(201).body(response);
    }
 
    @GetMapping
    public ResponseEntity<List<QueryDto.CategoryResponse>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }
}
package com.queryhub.query.controller;

import com.queryhub.query.dto.QueryDto;
import com.queryhub.query.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<QueryDto.CategoryResponse> createCategory(
            @Valid @RequestBody QueryDto.CategoryRequest request) {
        return ResponseEntity.status(201).body(categoryService.createCategory(request));
    }

    @GetMapping
    public ResponseEntity<List<QueryDto.CategoryResponse>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }
}

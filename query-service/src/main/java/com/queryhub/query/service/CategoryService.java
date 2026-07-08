package com.queryhub.query.service;

import com.queryhub.common.handler.ResourceNotFoundException;
import com.queryhub.query.dto.QueryDto;
import com.queryhub.query.entity.Category;
import com.queryhub.query.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public QueryDto.CategoryResponse createCategory(QueryDto.CategoryRequest request) {
        if (categoryRepository.existsByNameIgnoreCase(request.getName())) {
            throw new IllegalArgumentException("Category '" + request.getName() + "' already exists.");
        }

        Category category = Category.builder().name(request.getName()).build();
        categoryRepository.save(category);
        return new QueryDto.CategoryResponse(category.getId(), category.getName());
    }

    public List<QueryDto.CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(c -> new QueryDto.CategoryResponse(c.getId(), c.getName()))
                .collect(Collectors.toList());
    }

    public Category findByNameOrThrow(String name) {
        return categoryRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category '" + name + "' does not exist."));
    }
}

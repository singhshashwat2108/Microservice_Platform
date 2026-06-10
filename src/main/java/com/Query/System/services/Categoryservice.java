package com.Query.System.services; 

import com.Query.System.dto.QueryDto;
import com.Query.System.entity.Category;
import com.Query.System.repository.CategoryRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class Categoryservice {

    private final CategoryRepo categoryRepository;

    public Categoryservice(CategoryRepo categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    
    public QueryDto.CategoryResponse createCategory(QueryDto.CategoryRequest request) {

        if (categoryRepository.existsByNameIgnoreCase(request.getName())) {
            throw new IllegalArgumentException(
                "Category '" + request.getName() + "' already exists.");
        }

        Category category = new Category();
        category.setName(request.getName());
        categoryRepository.save(category);

        return new QueryDto.CategoryResponse(
                category.getId(),
                category.getName(),
                category.getCreatedAt());
    }

    public List<QueryDto.CategoryResponse> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(c -> new QueryDto.CategoryResponse(c.getId(), c.getName(), c.getCreatedAt()))
                .collect(Collectors.toList());
    }
}
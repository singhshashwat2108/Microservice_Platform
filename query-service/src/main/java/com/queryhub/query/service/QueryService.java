package com.queryhub.query.service;

import com.queryhub.common.handler.ResourceNotFoundException;
import com.queryhub.query.dto.QueryDto;
import com.queryhub.query.entity.Category;
import com.queryhub.query.entity.QueryEntity;
import com.queryhub.query.repository.QueryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QueryService {

    private static final Logger log = LoggerFactory.getLogger(QueryService.class);
    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final int MAX_PAGE_SIZE = 100;

    private final QueryRepository queryRepository;
    private final CategoryService categoryService;

    public QueryService(QueryRepository queryRepository, CategoryService categoryService) {
        this.queryRepository = queryRepository;
        this.categoryService = categoryService;
    }

    public QueryDto.QueryResponse createQuery(QueryDto.QueryRequest request, Long authorId, String authorName) {
        Category category = categoryService.findByNameOrThrow(request.getCategory());
        String description = request.getDescription().trim();
        String title = resolveTitle(request.getTitle(), description);

        QueryEntity query = QueryEntity.builder()
                .title(title)
                .description(description)
                .authorId(authorId)
                .authorName(authorName)
                .category(category)
                .build();

        queryRepository.save(query);
        log.info("Query created: id={}, authorId={}", query.getId(), authorId);
        return toResponse(query);
    }

    public QueryDto.QueryResponse updateQuery(Long id, QueryDto.QueryRequest request,
                                              Long authorId, String role) {
        QueryEntity query = findQueryOrThrow(id);
        checkOwnership(query, authorId, role);

        if (request.getCategory() != null && !request.getCategory().isBlank()) {
            query.setCategory(categoryService.findByNameOrThrow(request.getCategory()));
        }
        if (request.getDescription() != null && !request.getDescription().isBlank()) {
            query.setDescription(request.getDescription().trim());
            if (request.getTitle() == null || request.getTitle().isBlank()) {
                query.setTitle(resolveTitle(null, query.getDescription()));
            }
        }
        if (request.getTitle() != null && !request.getTitle().isBlank()) {
            query.setTitle(request.getTitle().trim());
        }

        queryRepository.save(query);
        log.info("Query updated: id={}", id);
        return toResponse(query);
    }

    public void deleteQuery(Long id, Long authorId, String role) {
        QueryEntity query = findQueryOrThrow(id);
        checkOwnership(query, authorId, role);
        queryRepository.delete(query);
        log.info("Query deleted: id={}", id);
    }

    public QueryDto.QueryResponse getQueryById(Long id) {
        return toResponse(findQueryOrThrow(id));
    }

    public QueryDto.PagedResponse<QueryDto.QueryResponse> getAllQueries(int page, int size, String categoryName) {
        Pageable pageable = PageRequest.of(page, normalizeSize(size), Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<QueryEntity> result;

        if (categoryName != null && !categoryName.isBlank()) {
            Category category = categoryService.findByNameOrThrow(categoryName);
            result = queryRepository.findByCategory(category, pageable);
        } else {
            result = queryRepository.findAll(pageable);
        }

        List<QueryDto.QueryResponse> content = result.getContent().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return new QueryDto.PagedResponse<>(
                content,
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages()
        );
    }

    public QueryDto.PagedResponse<QueryDto.QueryResponse> getLatestQueries(int page, int size) {
        return getAllQueries(page, size, null);
    }

    private QueryEntity findQueryOrThrow(Long id) {
        return queryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Query with id " + id + " not found."));
    }

    private void checkOwnership(QueryEntity query, Long authorId, String role) {
        if ("ADMIN".equals(role)) {
            return;
        }
        if (!query.getAuthorId().equals(authorId)) {
            throw new SecurityException("You are not the owner of this query.");
        }
    }

    private String resolveTitle(String title, String description) {
        if (title != null && !title.isBlank()) {
            return title.trim();
        }
        String trimmed = description.trim();
        return trimmed.length() <= 80 ? trimmed : trimmed.substring(0, 77) + "...";
    }

    private int normalizeSize(int size) {
        if (size <= 0) {
            return DEFAULT_PAGE_SIZE;
        }
        return Math.min(size, MAX_PAGE_SIZE);
    }

    private QueryDto.QueryResponse toResponse(QueryEntity query) {
        return new QueryDto.QueryResponse(
                query.getId(),
                query.getTitle(),
                query.getDescription(),
                query.getCategory().getName(),
                query.getAuthorId(),
                query.getAuthorName(),
                query.getCreatedAt(),
                query.getUpdatedAt()
        );
    }
}

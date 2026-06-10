package com.Query.System.services;

import com.Query.System.dto.QueryDto;
import com.Query.System.entity.Category;
import com.Query.System.entity.Query;
import com.Query.System.repository.CategoryRepo;
import com.Query.System.repository.QueryRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QueryService {

    private final QueryRepo queryRepository;
    private final CategoryRepo categoryRepository;

    public QueryService(QueryRepo queryRepository,
                        CategoryRepo categoryRepository) {
        this.queryRepository    = queryRepository;
        this.categoryRepository = categoryRepository;
    }

    public QueryDto.QueryResponse raiseQuery(QueryDto.QueryRequest request, String username) {
 
        Category category = categoryRepository
                .findByNameIgnoreCase(request.getTopic())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Category '" + request.getTopic() + "' does not exist. " +
                        "Please create it first at POST /category."));

        Query query = new Query();
        query.setQueryText(request.getQuery());
        query.setOwner(username);
        query.setCategory(category);
        queryRepository.save(query);

        return toResponse(query);
    }

    public QueryDto.QueryResponse editQuery(Long queryId,
                                            QueryDto.QueryRequest request,
                                            String username) {

        Query query = findQueryOrThrow(queryId);
        checkOwnership(query, username);
 
        if (request.getTopic() != null && !request.getTopic().isBlank()) {
            Category category = categoryRepository
                    .findByNameIgnoreCase(request.getTopic())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Category '" + request.getTopic() + "' does not exist."));
            query.setCategory(category);
        }

        if (request.getQuery() != null && !request.getQuery().isBlank()) {
            query.setQueryText(request.getQuery());
        }

        queryRepository.save(query);
        return toResponse(query);
    }

    public void deleteQuery(Long queryId, String username) {
        Query query = findQueryOrThrow(queryId);
        checkOwnership(query, username);
        queryRepository.delete(query);
    }

    public List<QueryDto.QueryResponse> getAllQueries() {
        return queryRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<QueryDto.QueryResponse> getQueriesByTopic(String topicName) {
        Category category = categoryRepository
                .findByNameIgnoreCase(topicName)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Category '" + topicName + "' does not exist."));

        return queryRepository.findByCategory(category)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private Query findQueryOrThrow(Long queryId) {
        return queryRepository.findById(queryId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Query with id " + queryId + " not found."));
    }

    private void checkOwnership(Query query, String username) {
        if (!query.getOwner().equals(username)) {
            throw new SecurityException(
                    "You are not the owner of this query.");
        }
    }

    private QueryDto.QueryResponse toResponse(Query query) {
        return new QueryDto.QueryResponse(
                query.getId(),
                query.getCategory().getName(),
                query.getQueryText(),
                query.getOwner(),
                query.getCreatedAt(),
                query.getUpdatedAt());
    }
}
package com.queryhub.query.controller;

import com.queryhub.common.security.JwtPrincipal;
import com.queryhub.query.dto.QueryDto;
import com.queryhub.query.service.QueryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/query")
public class QueryController {

    private final QueryService queryService;

    public QueryController(QueryService queryService) {
        this.queryService = queryService;
    }

    @PostMapping
    public ResponseEntity<QueryDto.QueryResponse> createQuery(
            @Valid @RequestBody QueryDto.QueryRequest request,
            @AuthenticationPrincipal JwtPrincipal principal) {
        QueryDto.QueryResponse response = queryService.createQuery(
                request, principal.getUserId(), principal.getUsername());
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QueryDto.QueryResponse> getQuery(@PathVariable Long id) {
        return ResponseEntity.ok(queryService.getQueryById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<QueryDto.PagedResponse<QueryDto.QueryResponse>> getAllQueries(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String category) {
        return ResponseEntity.ok(queryService.getAllQueries(page, size, category));
    }

    @GetMapping("/latest")
    public ResponseEntity<QueryDto.PagedResponse<QueryDto.QueryResponse>> getLatestQueries(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(queryService.getLatestQueries(page, size));
    }

    @PutMapping("/{id}")
    public ResponseEntity<QueryDto.QueryResponse> updateQuery(
            @PathVariable Long id,
            @RequestBody QueryDto.QueryRequest request,
            @AuthenticationPrincipal JwtPrincipal principal) {
        return ResponseEntity.ok(queryService.updateQuery(
                id, request, principal.getUserId(), principal.getRole()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteQuery(
            @PathVariable Long id,
            @AuthenticationPrincipal JwtPrincipal principal) {
        queryService.deleteQuery(id, principal.getUserId(), principal.getRole());
        return ResponseEntity.ok(Map.of("message", "Query deleted successfully."));
    }
}

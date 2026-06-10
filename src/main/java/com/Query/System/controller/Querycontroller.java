package com.Query.System.controller;

import com.Query.System.dto.QueryDto;
import com.Query.System.services.QueryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class Querycontroller {

    private final QueryService queryService;

    public Querycontroller(QueryService queryService) {
        this.queryService = queryService;
    }
 
    @PostMapping("/Query")
    public ResponseEntity<QueryDto.QueryResponse> raiseQuery(
            @Valid @RequestBody QueryDto.QueryRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        QueryDto.QueryResponse response =
                queryService.raiseQuery(request, userDetails.getUsername());
        return ResponseEntity.status(201).body(response);
    }
 
    @PutMapping("/Query/{id}")
    public ResponseEntity<QueryDto.QueryResponse> editQuery(
            @PathVariable Long id,
            @RequestBody QueryDto.QueryRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        QueryDto.QueryResponse response =
                queryService.editQuery(id, request, userDetails.getUsername());
        return ResponseEntity.ok(response);
    }
 
    @DeleteMapping("/Query/{id}")
    public ResponseEntity<Map<String, String>> deleteQuery(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        queryService.deleteQuery(id, userDetails.getUsername());
        return ResponseEntity.ok(Map.of("message", "Query deleted successfully."));
    }
 
    @GetMapping("/view")
    public ResponseEntity<List<QueryDto.QueryResponse>> viewAll() {
        return ResponseEntity.ok(queryService.getAllQueries());
    }
 
    @GetMapping(value = "/view", params = "topic")
    public ResponseEntity<List<QueryDto.QueryResponse>> viewByTopic(
            @RequestParam String topic) {
        return ResponseEntity.ok(queryService.getQueriesByTopic(topic));
    }
}
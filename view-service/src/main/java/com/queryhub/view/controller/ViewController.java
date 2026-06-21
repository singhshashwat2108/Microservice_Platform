package com.queryhub.view.controller;

import com.queryhub.view.dto.PagedViewResponse;
import com.queryhub.view.dto.QueryViewDto;
import com.queryhub.view.service.ViewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/view")
public class ViewController {

    private final ViewService viewService;

    public ViewController(ViewService viewService) {
        this.viewService = viewService;
    }

    @GetMapping("/query/{id}")
    public ResponseEntity<QueryViewDto> getQuery(@PathVariable Long id) {
        return ResponseEntity.ok(viewService.getQueryById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<PagedViewResponse<QueryViewDto>> getAllQueries(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String category) {
        return ResponseEntity.ok(viewService.getAllQueries(page, size, category));
    }

    @GetMapping("/latest")
    public ResponseEntity<PagedViewResponse<QueryViewDto>> getLatestQueries(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(viewService.getLatestQueries(page, size));
    }
}

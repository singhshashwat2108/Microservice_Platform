package com.queryhub.view.service;

import com.queryhub.view.client.QueryServiceClient;
import com.queryhub.view.dto.PagedViewResponse;
import com.queryhub.view.dto.QueryViewDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ViewService {

    private static final Logger log = LoggerFactory.getLogger(ViewService.class);

    private final QueryServiceClient queryServiceClient;
    private final CacheService cacheService;

    public ViewService(QueryServiceClient queryServiceClient, CacheService cacheService) {
        this.queryServiceClient = queryServiceClient;
        this.cacheService = cacheService;
    }

    @SuppressWarnings("unchecked")
    public QueryViewDto getQueryById(Long id) {
        log.info("Fetching query view for id={}", id);
        String key = "query:" + id;
        QueryViewDto cached = (QueryViewDto) cacheService.get(key);
        if (cached != null) {
            return cached;
        }

        QueryViewDto fresh = queryServiceClient.getQueryById(id);
        cacheService.put(key, fresh);
        return fresh;
    }

    @SuppressWarnings("unchecked")
    public PagedViewResponse<QueryViewDto> getAllQueries(int page, int size, String category) {
        log.info("Fetching all queries page={}, size={}, category={}", page, size, category);
        String key;
        if (category != null && !category.isBlank()) {
            key = "queries:category:" + category + ":" + page + ":" + size;
        } else {
            key = "queries:all:" + page + ":" + size;
        }

        PagedViewResponse<QueryViewDto> cached = (PagedViewResponse<QueryViewDto>) cacheService.get(key);
        if (cached != null) {
            return cached;
        }

        PagedViewResponse<QueryViewDto> fresh = queryServiceClient.getAllQueries(page, size, category);
        cacheService.put(key, fresh);
        return fresh;
    }

    @SuppressWarnings("unchecked")
    public PagedViewResponse<QueryViewDto> getLatestQueries(int page, int size) {
        log.info("Fetching latest queries page={}, size={}", page, size);
        String key = "queries:latest:" + page + ":" + size;
        PagedViewResponse<QueryViewDto> cached = (PagedViewResponse<QueryViewDto>) cacheService.get(key);
        if (cached != null) {
            return cached;
        }

        PagedViewResponse<QueryViewDto> fresh = queryServiceClient.getLatestQueries(page, size);
        cacheService.put(key, fresh);
        return fresh;
    }
}

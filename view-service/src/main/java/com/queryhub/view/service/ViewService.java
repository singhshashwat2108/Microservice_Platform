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

    public ViewService(QueryServiceClient queryServiceClient) {
        this.queryServiceClient = queryServiceClient;
    }

    public QueryViewDto getQueryById(Long id) {
        log.info("Fetching query view for id={}", id);
        return queryServiceClient.getQueryById(id);
    }

    public PagedViewResponse<QueryViewDto> getAllQueries(int page, int size, String category) {
        log.info("Fetching all queries page={}, size={}, category={}", page, size, category);
        return queryServiceClient.getAllQueries(page, size, category);
    }

    public PagedViewResponse<QueryViewDto> getLatestQueries(int page, int size) {
        log.info("Fetching latest queries page={}, size={}", page, size);
        return queryServiceClient.getLatestQueries(page, size);
    }
}

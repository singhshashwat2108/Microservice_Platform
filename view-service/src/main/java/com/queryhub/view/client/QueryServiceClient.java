package com.queryhub.view.client;

import com.queryhub.view.dto.PagedViewResponse;
import com.queryhub.view.dto.QueryViewDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class QueryServiceClient {

    private final RestClient restClient;

    public QueryServiceClient(@Value("${query.service.url}") String queryServiceUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(queryServiceUrl)
                .build();
    }

    public QueryViewDto getQueryById(Long id) {
        return restClient.get()
                .uri("/query/{id}", id)
                .retrieve()
                .body(QueryViewDto.class);
    }

    public PagedViewResponse<QueryViewDto> getAllQueries(int page, int size, String category) {
        return restClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path("/query/all")
                            .queryParam("page", page)
                            .queryParam("size", size);
                    if (category != null && !category.isBlank()) {
                        uriBuilder.queryParam("category", category);
                    }
                    return uriBuilder.build();
                })
                .retrieve()
                .body(new ParameterizedTypeReference<PagedViewResponse<QueryViewDto>>() {});
    }

    public PagedViewResponse<QueryViewDto> getLatestQueries(int page, int size) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder.path("/query/latest")
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<PagedViewResponse<QueryViewDto>>() {});
    }
}

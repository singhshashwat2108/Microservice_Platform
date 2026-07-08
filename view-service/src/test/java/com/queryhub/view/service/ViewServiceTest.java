package com.queryhub.view.service;

import com.queryhub.view.client.QueryServiceClient;
import com.queryhub.view.dto.PagedViewResponse;
import com.queryhub.view.dto.QueryViewDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ViewServiceTest {

    @Mock
    private QueryServiceClient queryServiceClient;
    @Mock
    private CacheService cacheService;

    @InjectMocks
    private ViewService viewService;

    @Test
    void getLatestQueries_delegatesToClient() {
        PagedViewResponse<QueryViewDto> expected = new PagedViewResponse<>(List.of(), 0, 20, 0, 0);
        when(queryServiceClient.getLatestQueries(0, 20)).thenReturn(expected);

        PagedViewResponse<QueryViewDto> result = viewService.getLatestQueries(0, 20);

        assertEquals(expected, result);
        verify(queryServiceClient).getLatestQueries(0, 20);
    }
}

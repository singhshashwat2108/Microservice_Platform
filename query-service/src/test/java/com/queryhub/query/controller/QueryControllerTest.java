package com.queryhub.query.controller;

import com.queryhub.query.dto.QueryDto;
import com.queryhub.query.service.QueryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(QueryController.class)
@AutoConfigureMockMvc(addFilters = false)
class QueryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private QueryService queryService;

    @Test
    void getQueryById_returnsQuery() throws Exception {
        QueryDto.QueryResponse response = new QueryDto.QueryResponse(
                1L, "Title", "Description", "Tech", 1L, "alice",
                LocalDateTime.now(), LocalDateTime.now()
        );
        when(queryService.getQueryById(1L)).thenReturn(response);

        mockMvc.perform(get("/query/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.topic").value("Tech"));
    }

    @Test
    void getAllQueries_returnsPagedResult() throws Exception {
        QueryDto.PagedResponse<QueryDto.QueryResponse> paged = new QueryDto.PagedResponse<>(
                List.of(), 0, 20, 0, 0
        );
        when(queryService.getAllQueries(0, 20, null)).thenReturn(paged);

        mockMvc.perform(get("/query/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page").value(0));
    }
}

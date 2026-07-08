package com.queryhub.view.controller;

import com.queryhub.view.dto.PagedViewResponse;
import com.queryhub.view.dto.QueryViewDto;
import com.queryhub.view.service.ViewService;
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

@WebMvcTest(ViewController.class)
@AutoConfigureMockMvc(addFilters = false)
class ViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ViewService viewService;

    @Test
    void getQueryById_returnsQuery() throws Exception {
        QueryViewDto dto = new QueryViewDto();
        dto.setId(1L);
        dto.setTopic("Tech");
        dto.setDescription("Sample");
        dto.setOwner("alice");
        dto.setCreatedAt(LocalDateTime.now());

        when(viewService.getQueryById(1L)).thenReturn(dto);

        mockMvc.perform(get("/view/query/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.topic").value("Tech"));
    }

    @Test
    void getAllQueries_returnsPagedData() throws Exception {
        when(viewService.getAllQueries(0, 20, null))
                .thenReturn(new PagedViewResponse<>(List.of(), 0, 20, 0, 0));

        mockMvc.perform(get("/view/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page").value(0));
    }
}

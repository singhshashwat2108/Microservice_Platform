package com.queryhub.query.service;

import com.queryhub.query.dto.QueryDto;
import com.queryhub.query.entity.Category;
import com.queryhub.query.entity.QueryEntity;
import com.queryhub.query.repository.QueryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QueryServiceTest {

    @Mock
    private QueryRepository queryRepository;
    @Mock
    private CategoryService categoryService;
    @Mock
    private com.queryhub.query.repository.CommentRepository commentRepository;
    @Mock
    private com.queryhub.query.repository.LikeRepository likeRepository;
    @Mock
    private CacheInvalidationService cacheInvalidationService;
    @Mock
    private com.queryhub.query.kafka.KafkaEventPublisher kafkaEventPublisher;

    @InjectMocks
    private QueryService queryService;

    @Test
    void deleteQuery_rejectsNonOwner() {
        QueryEntity query = QueryEntity.builder()
                .id(1L)
                .authorId(10L)
                .authorName("alice")
                .build();

        when(queryRepository.findById(1L)).thenReturn(Optional.of(query));

        assertThrows(SecurityException.class,
                () -> queryService.deleteQuery(1L, 99L, "USER"));
    }

    @Test
    void createQuery_savesAndReturnsResponse() {
        QueryDto.QueryRequest request = new QueryDto.QueryRequest();
        request.setCategory("Tech");
        request.setDescription("How does JWT work?");

        Category category = Category.builder().id(1L).name("Tech").build();
        when(categoryService.findByNameOrThrow("Tech")).thenReturn(category);
        when(queryRepository.save(any(QueryEntity.class))).thenAnswer(inv -> {
            QueryEntity entity = inv.getArgument(0);
            entity.setId(5L);
            return entity;
        });

        QueryDto.QueryResponse response = queryService.createQuery(request, 1L, "alice");

        assertEquals(5L, response.getId());
        assertEquals("alice", response.getOwner());
        assertEquals("Tech", response.getTopic());
    }
}

package com.oing.domain;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/12/05
 * Time: 12:45 PM
 */
public class PaginationDTOTest {
    @Test
    void testPaginationDTOCreation() {
        // Given
        List<String> testData = Arrays.asList("Item1", "Item2", "Item3");
        Page<String> page = new PageImpl<>(testData, PageRequest.of(0, testData.size()), testData.size());

        // When
        PaginationDTO<String> paginationDTO = PaginationDTO.of(page);

        // Then
        assertEquals(testData, paginationDTO.results());
    }
}

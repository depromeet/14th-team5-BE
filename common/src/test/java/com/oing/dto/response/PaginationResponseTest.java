package com.oing.dto.response;

import com.oing.domain.PaginationDTO;
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
 * Time: 12:46 PM
 */
public class PaginationResponseTest {
    @Test
    void testPaginationResponseCreation() {
        // Given
        List<String> testData = Arrays.asList("data1", "data2", "data3");
        Page<String> page = new PageImpl<>(testData, PageRequest.of(0, testData.size()), testData.size());

        // When
        PaginationDTO<String> paginationDTO = PaginationDTO.of(page);
        PaginationResponse<String> paginationResponse = PaginationResponse.of(paginationDTO, 1, testData.size());

        // Then
        assertEquals(1, paginationResponse.currentPage());
        assertEquals(paginationDTO.totalPage(), paginationResponse.totalPage());
        assertEquals(testData, paginationResponse.results());
    }

    @Test
    void testPaginationResponseMapping() {
        // Given
        List<String> testData = Arrays.asList("data1", "data2", "data3");
        Page<String> page = new PageImpl<>(testData, PageRequest.of(0, testData.size()), testData.size());

        // When
        PaginationDTO<String> paginationDTO = PaginationDTO.of(page);
        PaginationResponse<String> paginationResponse = PaginationResponse.of(paginationDTO, 1, testData.size());
        PaginationResponse<Integer> mappedResponse = paginationResponse.map(String::length);

        // Then
        assertEquals(paginationResponse.currentPage(), mappedResponse.currentPage());
        assertEquals(paginationResponse.totalPage(), mappedResponse.totalPage());
        assertEquals(paginationResponse.itemPerPage(), mappedResponse.itemPerPage());
        assertEquals(Arrays.asList(5, 5, 5), mappedResponse.results());
    }
}

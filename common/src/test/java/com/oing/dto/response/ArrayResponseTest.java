package com.oing.dto.response;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/12/05
 * Time: 12:46 PM
 */
public class ArrayResponseTest {
    @Test
    public void testArrayResponseCreation() {
        // Given
        List<String> testData = Arrays.asList("data1", "data2", "data3");

        // When
        ArrayResponse<String> arrayResponse = ArrayResponse.of(testData);

        // Then
        assertEquals(testData, arrayResponse.results());
    }
}

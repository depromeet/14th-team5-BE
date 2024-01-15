package com.oing.dto.response;

import com.oing.domain.PaginationDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Collection;
import java.util.function.Function;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/12/05
 * Time: 12:30 PM
 */
public record PaginationResponse<T>(
        int currentPage,
        int totalPage,
        int itemPerPage,
        boolean hasNext,
        Collection<T> results
) {
    public static <T> PaginationResponse<T> of(PaginationDTO<T> dto, int currentPage, int itemPerPage) {
        return new PaginationResponse<>(currentPage, dto.totalPage(), itemPerPage, dto.totalPage() > currentPage, dto.results());
    }

    public <R> PaginationResponse<R> map(Function<T, R> mappingFunction) {
        Collection<R> mappedResults = results.stream().map(mappingFunction).toList();
        return new PaginationResponse<>(currentPage, totalPage, itemPerPage, hasNext, mappedResults);
    }
}

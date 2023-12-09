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
@Schema(description = "페이지네이션 응답")
public record PaginationResponse<T>(
        @Schema(description = "현재 페이지", example = "1")
        int currentPage,

        @Schema(description = "전체 페이지 수", example = "30")
        int totalPage,

        @Schema(description = "페이지당 데이터 수", example = "10")
        int itemPerPage,

        @Schema(description = "실제 데이터 컬렉션", example = "[\"data\"]")
        Collection<T> results
) {
        public static <T> PaginationResponse<T> of(PaginationDTO<T> dto, int currentPage, int itemPerPage) {
                return new PaginationResponse<>(currentPage, dto.totalPage(), itemPerPage, dto.results());
        }

        public <R> PaginationResponse<R> map(Function<T, R> mappingFunction) {
                Collection<R> mappedResults = results.stream().map(mappingFunction).toList();
                return new PaginationResponse<>(currentPage, totalPage, itemPerPage, mappedResults);
        }
}

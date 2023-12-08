package com.oing.domain;

import org.springframework.data.domain.Page;

import java.util.Collection;

public record PaginationDTO<T>(
        int totalPage,
        Collection<T> results
) {
    public static <T> PaginationDTO<T> of(Page<T> page) {
        return new PaginationDTO<>(page.getTotalPages(), page.getContent());
    }
}

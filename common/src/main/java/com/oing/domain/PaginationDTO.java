package com.oing.domain;

import org.springframework.data.domain.Page;

import java.util.Collection;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/12/05
 * Time: 12:37 PM
 */
public record PaginationDTO<T>(
        int totalPage,
        Collection<T> results
) {
    public static <T> PaginationDTO<T> of(Page<T> page) {
        return new PaginationDTO<>(page.getTotalPages(), page.getContent());
    }
}

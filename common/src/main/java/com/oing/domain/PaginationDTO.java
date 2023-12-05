package com.oing.domain;

import org.springframework.data.domain.Page;

import java.util.Collection;

<<<<<<< HEAD
=======
/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/12/05
 * Time: 12:37 PM
 */
>>>>>>> 0e9c512 (feat: add Pagination Response and DTO)
public record PaginationDTO<T>(
        int totalPage,
        Collection<T> results
) {
    public static <T> PaginationDTO<T> of(Page<T> page) {
        return new PaginationDTO<>(page.getTotalPages(), page.getContent());
    }
}

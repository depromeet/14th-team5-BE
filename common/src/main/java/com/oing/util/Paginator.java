package com.oing.util;

import com.oing.domain.PaginationDTO;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Paginator<T> {
    private final List<T> items;
    private final int pageSize;

    public Paginator(List<T> items, int pageSize) {
        if (pageSize <= 0) {
            throw new IllegalArgumentException("Page size must be greater than 0.");
        }
        this.items = Optional.ofNullable(items).orElse(Collections.emptyList());
        this.pageSize = pageSize;
    }

    public PaginationDTO<T> getPage(int pageNumber) {
        if (pageNumber <= 0) {
            throw new IllegalArgumentException("Page number must be greater than 0.");
        }

        int totalItems = items.size();
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);

        if (pageNumber > totalPages) {
            return new PaginationDTO<>(totalPages, Collections.emptyList());
        }

        int startIndex = (pageNumber - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, totalItems);

        List<T> pageItems = items.subList(startIndex, endIndex);
        return new PaginationDTO<>(totalPages, pageItems);
    }
}

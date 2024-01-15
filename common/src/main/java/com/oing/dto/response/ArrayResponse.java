package com.oing.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Collection;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/12/05
 * Time: 12:30 PM
 */
public record ArrayResponse<T>(
        Collection<T> results
) {
    public static <T> ArrayResponse<T> of(Collection<T> results) {
        return new ArrayResponse<>(results);
    }
}

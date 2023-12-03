package com.oing.domain;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/11/27
 * Time: 12:57 PM
 */
public record ErrorReportDTO(
        String errorMessage,
        String payload
) {
}

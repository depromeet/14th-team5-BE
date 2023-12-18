package com.oing.domain;

import java.time.LocalDateTime;

public record MemberPostCountDTO(
        LocalDateTime createdAt,
        long count
) {
}

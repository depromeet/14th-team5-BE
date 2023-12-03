package com.oing.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/11/27
 * Time: 1:44 PM
 */
class BaseAuditEntityTest {
    @DisplayName("BaseAuditEntity 클래스 인스턴스 생성시 기본값은 null이어야 한다")
    @Test
    void fieldsShouldBeNullOnNewInstance() {
        // Given
        BaseAuditEntity baseAuditEntity = new BaseAuditEntity();

        // When

        // Then
        assertNull(baseAuditEntity.getCreatedAt());
        assertNull(baseAuditEntity.getUpdatedAt());
    }
}

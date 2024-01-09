package com.oing.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.util.UUID;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 1/4/24
 * Time: 11:48 AM
 */
@Entity(name = "app_version")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class AppVersion extends BaseAuditEntity {
    @Id
    @JdbcTypeCode(Types.VARCHAR)
    @Column(name = "app_key", columnDefinition = "CHAR(36)")
    private UUID appKey;

    @Column(name = "app_version", columnDefinition = "VARCHAR(36)", nullable = false)
    private String appVersion;

    @Column(name = "in_service")
    private boolean inService;

    @Column(name = "in_review")
    private boolean inReview;
}

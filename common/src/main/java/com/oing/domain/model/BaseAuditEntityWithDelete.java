package com.oing.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public class BaseAuditEntityWithDelete extends BaseAuditEntity {
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public void updateDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }
}

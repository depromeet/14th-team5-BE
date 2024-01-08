package com.oing.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public class DeletableBaseAuditEntity extends BaseAuditEntity {
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public void updateDeletedAt() {
        this.deletedAt = LocalDateTime.now();
    }
}

package com.oing.domain;

import jakarta.persistence.MappedSuperclass;

import java.util.Comparator;

@MappedSuperclass
public abstract class BaseComment extends BaseAuditEntity {
    public abstract String getContent();
    public abstract String getMemberId();
    public abstract String getId();

    public static Comparator<BaseComment> getComparator(String sort) {
        if (sort == null || sort.equalsIgnoreCase("ASC")) {
            return Comparator.comparing(BaseComment::getCreatedAt);
        } else {
            return Comparator.comparing(BaseComment::getCreatedAt).reversed();
        }
    }
}

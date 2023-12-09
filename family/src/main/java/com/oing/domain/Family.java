package com.oing.domain;

import com.oing.domain.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "family")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Getter
public class Family extends BaseEntity {

    @Id
    @Column(name = "family_id", columnDefinition = "CHAR(26)", nullable = false)
    private String id;
}

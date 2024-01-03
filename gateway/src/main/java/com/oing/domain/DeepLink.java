package com.oing.domain;

import com.oing.domain.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "deep_link")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class DeepLink extends BaseEntity {
    @Id
    @Column(name = "link_id", columnDefinition = "CHAR(8)", nullable = false)
    private String linkId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", columnDefinition = "VARCHAR(64)", nullable = false)
    private DeepLinkType type;
}

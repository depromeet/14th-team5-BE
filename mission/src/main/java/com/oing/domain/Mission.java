package com.oing.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Getter
@Builder
@Entity(name = "mission")
public class Mission extends BaseAuditEntity {
    @Id
    @Column(name = "mission_id", columnDefinition = "CHAR(26)", nullable = false)
    private String id;

    @Column(nullable = false)
    private String content;


    public void updateContent(String content) {
        this.content = content;
    }
}

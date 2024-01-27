package com.oing.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "family")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Family extends BaseEntity {

    @Id
    @Column(name = "family_id", columnDefinition = "CHAR(26)", nullable = false)
    private String id;

    @Column(name = "score", nullable = false)
    private Integer score = 0;


    public Family(String id) {
        this.id = id;
    }

    public void addAllFamilyMembersUploadedScore() {
        this.score += 10;
    }

    public void addNewPostScore() {
        this.score += 5;
    }

    public void addNewReactionScore() {
        this.score += 1;
    }
}

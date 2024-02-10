package com.oing.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity(name = "family_top_percentage_history")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class FamilyTopPercentageHistory {

    @EmbeddedId
    private FamilyTopPercentageHistoryId familyTopPercentageHistoryId;

    @MapsId("familyId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_id", referencedColumnName = "family_id", columnDefinition = "CHAR(26)", nullable = false)
    private Family family;

    @Column(nullable = false)
    private Integer topPercentage;
}
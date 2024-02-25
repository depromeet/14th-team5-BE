package com.oing.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


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

    public FamilyTopPercentageHistory(CreateNewFamilyTopPercentageHistoryDTO dto) {
        this.familyTopPercentageHistoryId = new FamilyTopPercentageHistoryId(dto.familyId(), dto.year(), dto.month());
        this.family = dto.family();
        this.topPercentage = dto.topPercentage();
    }
}
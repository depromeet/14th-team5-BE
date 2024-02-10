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

    @Id @JoinColumn(name = "family_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Family family;

    @Id @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private Integer topPercentage;
}
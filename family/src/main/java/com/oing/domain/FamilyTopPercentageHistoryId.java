package com.oing.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@EqualsAndHashCode
public class FamilyTopPercentageHistoryId implements Serializable {

    @Column(name = "family_id", columnDefinition = "CHAR(26)", nullable = false)
    private String familyId;

    @Column(name = "history_year", nullable = false)
    private Integer year;

    @Column(name = "history_month", nullable = false)
    private Integer month;
}

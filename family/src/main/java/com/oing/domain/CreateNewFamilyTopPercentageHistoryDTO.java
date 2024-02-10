package com.oing.domain;

import java.time.LocalDate;

public record CreateNewFamilyTopPercentageHistoryDTO(
        String familyId,
        LocalDate date,
        Family family,
        Integer topPercentage
) {
}

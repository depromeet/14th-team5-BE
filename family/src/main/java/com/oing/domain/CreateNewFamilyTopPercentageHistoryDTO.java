package com.oing.domain;

public record CreateNewFamilyTopPercentageHistoryDTO(
        String familyId,
        Integer year,
        Integer month,
        Family family,
        Integer topPercentage
) {
}

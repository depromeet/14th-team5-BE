package com.oing.service;

import com.oing.repository.FamilyTopPercentageHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FamilyTopPercentageHistoryService {

    private final FamilyTopPercentageHistoryRepository familyTopPercentageHistoryRepository;
}

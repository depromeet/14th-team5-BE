package com.oing.service;

import com.oing.domain.MemberQuitReason;
import com.oing.domain.MemberQuitReasonType;
import com.oing.repository.MemberQuitReasonRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MemberQuitReasonService {
    private final MemberQuitReasonRepository memberQuitReasonRepository;

    @Transactional
    public void recordMemberQuitReason(String memberId, List<MemberQuitReasonType> reasonIds) {
        List<MemberQuitReason> records = reasonIds
                .stream()
                .map(reasonId -> new MemberQuitReason(memberId, reasonId))
                .collect(Collectors.toList());
        memberQuitReasonRepository.saveAll(records);
    }
}

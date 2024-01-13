package com.oing.service;

import com.oing.domain.MemberQuitReason;
import com.oing.domain.MemberQuitReasonType;
import com.oing.repository.MemberQuitReasonRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberQuitReasonService {
    private final MemberQuitReasonRepository memberQuitReasonRepository;

    @Transactional
    public MemberQuitReason recordMemberQuitReason(String memberId, MemberQuitReasonType reasonId) {
        return memberQuitReasonRepository.save(
                new MemberQuitReason(
                   memberId,
                   reasonId
                )
        );
    }
}

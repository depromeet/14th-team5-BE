package com.oing.service;

import com.oing.domain.MemberPick;
import com.oing.exception.AlreadyPickedMemberException;
import com.oing.repository.MemberPickRepository;
import com.oing.util.IdentityGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 4/2/24
 * Time: 11:58 AM
 */
@RequiredArgsConstructor
@Service
public class MemberPickService {
    private final MemberPickRepository memberPickRepository;
    private final IdentityGenerator identityGenerator;

    @Transactional
    public MemberPick pickMember(String familyId, String fromMemberId, String toMemberId) {
        LocalDate today = LocalDate.now();
        MemberPick priorMemberPick = memberPickRepository
                .findByFamilyIdAndFromMemberIdAndDateAndToMemberId(familyId, fromMemberId, today, toMemberId);
        if(priorMemberPick != null) {
            throw new AlreadyPickedMemberException();
        }

        MemberPick newMemberPick = new MemberPick(
                identityGenerator.generateIdentity(),
                familyId,
                fromMemberId,
                today,
                toMemberId
        );
        return memberPickRepository.save(newMemberPick);
    }
}

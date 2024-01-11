package com.oing.service;

import com.oing.domain.Member;
import com.oing.exception.FamilyNotFoundException;
import com.oing.exception.MemberNotFoundException;
import com.oing.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 1/11/24
 * Time: 10:21 AM
 */
@RequiredArgsConstructor
@Service
public class MemberBridgeImpl implements MemberBridge {
    private final MemberRepository memberRepository;

    @Override
    public String getFamilyIdByMemberId(String memberId) {
        Member member = memberRepository
                .findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
        String familyId = member.getFamilyId();
        if (familyId == null) throw new FamilyNotFoundException();
        return familyId;
    }
}

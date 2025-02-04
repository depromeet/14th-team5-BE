package com.oing.service;

import com.oing.domain.Member;
import com.oing.exception.FamilyNotFoundException;
import com.oing.exception.MemberNotFoundException;
import com.oing.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

    @Transactional
    @Override
    public boolean isInSameFamily(String memberIdFirst, String memberIdSecond) {
        Member firstMember = memberRepository
                .findById(memberIdFirst)
                .orElseThrow(MemberNotFoundException::new);

        Member secondMember = memberRepository
                .findById(memberIdSecond)
                .orElseThrow(MemberNotFoundException::new);

        return firstMember.hasFamily() && secondMember.hasFamily() &&
                firstMember.getFamilyId().equals(secondMember.getFamilyId());
    }

    @Override
    public boolean isDeletedMember(String memberId) {
        return memberRepository.existsByIdAndDeletedAtNotNull(memberId);
    }

    @Override
    public boolean isBirthDayMember(String memberId) {
        Optional<Member> memberOptional = memberRepository.findById(memberId);
        if (memberOptional.isEmpty()) throw new MemberNotFoundException();

        return memberOptional.get().getDayOfBirth().equals(LocalDate.now());
    }

    @Override
    public List<String> getFamilyMembersIdsByFamilyId(String familyId) {
        return memberRepository.findAllByFamilyIdAndDeletedAtIsNull(familyId).stream()
                .map(Member::getId)
                .toList();
    }

    @Override
    public String getMemberNameByMemberId(String memberId) {
        return memberRepository.findById(memberId)
                .map(Member::getName)
                .orElseThrow(MemberNotFoundException::new);
    }

    @Override
    public List<String> getFamilyMemberNamesByFamilyId(String familyId) {
        return memberRepository.findFamilyMemberNamesByFamilyId(familyId);
    }

    @Override
    public List<String> getFamilyMemberProfileImgUrlsByFamilyId(String familyId) {
        return memberRepository.findFamilyMemberProfileImgUrlsByFamilyId(familyId);
    }

    @Override
    public String getMemberProfileImgUrlByMemberId(String memberId) {
        return memberRepository.findById(memberId)
                .map(Member::getProfileImgUrl)
                .orElseThrow(MemberNotFoundException::new);
    }

    @Override
    public int getFamilyMemberCountByFamilyId(String familyId) {
        return memberRepository.countByFamilyIdAndDeletedAtIsNull(familyId);
    }
}

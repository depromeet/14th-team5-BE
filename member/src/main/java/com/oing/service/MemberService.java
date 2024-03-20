package com.oing.service;

import com.oing.domain.CreateNewUserDTO;
import com.oing.domain.Member;
import com.oing.domain.SocialLoginProvider;
import com.oing.domain.SocialMember;
import com.oing.domain.key.SocialMemberKey;
import com.oing.dto.request.QuitMemberRequest;
import com.oing.dto.response.FamilyMemberProfileResponse;
import com.oing.exception.FamilyNotFoundException;
import com.oing.exception.MemberNotFoundException;
import com.oing.repository.MemberRepository;
import com.oing.repository.SocialMemberRepository;
import com.oing.util.IdentityGenerator;
import com.oing.util.PreSignedUrlGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final SocialMemberRepository socialMemberRepository;
    private final MemberDeviceService memberDeviceService;
    private final MemberQuitReasonService memberQuitReasonService;

    private final PreSignedUrlGenerator preSignedUrlGenerator;
    private final IdentityGenerator identityGenerator;


    @Transactional
    public Member createNewMember(CreateNewUserDTO createNewUserDTO) {
        //사용자 생성
        Member member = Member
                .builder()
                .id(identityGenerator.generateIdentity())
                .dayOfBirth(createNewUserDTO.dayOfBirth())
                .profileImgUrl(createNewUserDTO.profileImgUrl())
                .profileImgKey(preSignedUrlGenerator.extractImageKey(createNewUserDTO.profileImgUrl()))
                .name(createNewUserDTO.memberName())
                .build();
        memberRepository.save(member);

        //사용자 소셜 로그인 정보 링크
        SocialMember socialMember = new SocialMember(
                createNewUserDTO.provider(),
                createNewUserDTO.identifier(),
                member);
        socialMemberRepository.save(socialMember);

        return member;
    }


    public Member getMemberByMemberId(String memberId) {
        return memberRepository
                .findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
    }

    public String getFamilyIdByMemberId(String memberId) {
        Member member = getMemberByMemberId(memberId);
        if (member.getFamilyId() == null) {
            throw new FamilyNotFoundException();
        }

        return member.getFamilyId();
    }

    public Optional<Member> getMemberBySocialMemberKey(SocialLoginProvider provider, String identifier) {
        SocialMemberKey key = new SocialMemberKey(provider, identifier);
        return socialMemberRepository
                .findById(key)
                .map(SocialMember::getMember);
    }

    public List<String> getFamilyMembersIdsByFamilyId(String familyId) {
        return memberRepository
                .findAllByFamilyIdAndDeletedAtIsNull(familyId)
                .stream()
                .map(Member::getId)
                .toList();
    }

    public List<String> getFamilyMembersIdsByFamilyJoinAtBefore(String familyId, LocalDate date) {
        return memberRepository.findAllByFamilyIdAndFamilyJoinAtBefore(familyId, date.atStartOfDay())
                .stream()
                .map(Member::getId)
                .toList();
    }

    public Page<FamilyMemberProfileResponse> getFamilyMembersProfilesByFamilyId(
            String familyId, int page, int size
    ) {
        Page<Member> memberPage = memberRepository.findAllByFamilyIdAndDeletedAtIsNull(familyId, PageRequest.of(page - 1, size));

        List<Member> members = memberPage.getContent();
        List<FamilyMemberProfileResponse> familyMemberProfiles = members
                .stream()
                .map(FamilyMemberProfileResponse::of)
                .toList();

        return new PageImpl<>(familyMemberProfiles, memberPage.getPageable(), memberPage.getTotalElements());
    }

    public boolean isFamilyMember(String memberId, String familyId) {
        Member member = getMemberByMemberId(memberId);
        return member.getFamilyId().equals(familyId);
    }

    public List<Member> findAllActiveMembers() {
        return memberRepository.findAllByDeletedAtIsNull();
    }


    @Transactional
    public Member updateMemberProfileImageUrl(String memberId, String profileImageUrl) {
        Member member = getMemberByMemberId(memberId);

        String profileImgKey = preSignedUrlGenerator.extractImageKey(profileImageUrl);
        member.updateProfileImg(profileImageUrl, profileImgKey);

        return member;
    }

    @Transactional
    public Member updateMemberName(String memberId, String name) {
        Member member = getMemberByMemberId(memberId);

        member.updateName(name);
        return member;
    }


    @Transactional
    public void deleteMember(String memberId, QuitMemberRequest quitReason) {
        Member member = getMemberByMemberId(memberId);

        deleteAllSocialMembersByMember(memberId);
        member.deleteMemberInfo();

        if (quitReason != null) { //For Api Version Compatibility
            memberQuitReasonService.recordMemberQuitReason(memberId, quitReason.reasonIds());
        }

        memberDeviceService.removeAllDevicesByMemberId(memberId);
    }

    @Transactional
    public Member deleteMemberProfileImageUrl(String memberId) {
        Member member = getMemberByMemberId(memberId);

        member.deleteProfileImg();

        return member;
    }

    public void deleteAllSocialMembersByMember(String memberId) {
        socialMemberRepository.deleteAllByMemberId(memberId);
    }
}

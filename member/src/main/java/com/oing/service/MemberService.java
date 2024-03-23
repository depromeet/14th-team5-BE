package com.oing.service;

import com.oing.domain.CreateNewUserDTO;
import com.oing.domain.Member;
import com.oing.domain.SocialLoginProvider;
import com.oing.domain.SocialMember;
import com.oing.domain.key.SocialMemberKey;
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
    private final PreSignedUrlGenerator preSignedUrlGenerator;
    private final IdentityGenerator identityGenerator;

    public Member findMemberById(String memberId) {
        return memberRepository
                .findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
    }

    public String findFamilyIdByMemberId(String memberId) {
        Member findMember = findMemberById(memberId);
        if (findMember.getFamilyId() == null) {
            throw new FamilyNotFoundException();
        }
        return findMember.getFamilyId();
    }

    @Transactional
    public Optional<Member> findMemberBySocialMemberKey(SocialLoginProvider provider, String identifier) {
        SocialMemberKey key = new SocialMemberKey(provider, identifier);
        return socialMemberRepository
                .findById(key)
                .map(SocialMember::getMember);
    }

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

    public List<String> findFamilyMembersIdsByFamilyId(String familyId) {
        return memberRepository
                .findAllByFamilyIdAndDeletedAtIsNull(familyId)
                .stream()
                .map(Member::getId)
                .toList();
    }

    public List<String> findFamilyMembersIdsByFamilyJoinAtBefore(String familyId, LocalDate date) {
        return memberRepository
                .findAllByFamilyIdAndFamilyJoinAtBeforeAndDeletedAtIsNull(familyId, date.atStartOfDay())
                .stream()
                .map(Member::getId)
                .toList();
    }

    @Transactional
    public Page<FamilyMemberProfileResponse> findFamilyMembersProfilesByFamilyId(
            String familyId, int page, int size
    ) {
        Page<Member> memberPage = memberRepository.findAllByFamilyIdAndDeletedAtIsNull(familyId, PageRequest.of(page - 1, size));
        List<Member> members = memberPage.getContent();

        List<FamilyMemberProfileResponse> familyMemberProfiles = createFamilyMemberProfiles(members);

        return new PageImpl<>(familyMemberProfiles, memberPage.getPageable(), memberPage.getTotalElements());
    }

    private List<FamilyMemberProfileResponse> createFamilyMemberProfiles(List<Member> members) {
        return members.stream()
                .map(FamilyMemberProfileResponse::of)
                .toList();
    }

    public void deleteAllSocialMembersByMember(String memberId) {
        socialMemberRepository.deleteAllByMemberId(memberId);
    }

    public List<Member> findAllMember() {
        return memberRepository.findAllByDeletedAtIsNull();
    }
}

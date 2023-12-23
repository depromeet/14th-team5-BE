package com.oing.service;

import com.oing.domain.CreateNewUserDTO;
import com.oing.domain.SocialLoginProvider;
import com.oing.domain.model.Member;
import com.oing.domain.model.SocialMember;
import com.oing.domain.model.key.SocialMemberKey;
import com.oing.dto.response.FamilyMemberProfileResponse;
import com.oing.exception.MemberNotFoundException;
import com.oing.repository.MemberRepository;
import com.oing.repository.SocialMemberRepository;
import com.oing.util.IdentityGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final SocialMemberRepository socialMemberRepository;

    private final IdentityGenerator identityGenerator;

    public Member findMemberById(String memberId) {
        return memberRepository
                .findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
    }

    public String findFamilyIdByMemberId(String memberId) {
        return findMemberById(memberId).getFamilyId();
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

    public List<String> findFamilyMembersIdByMemberId(String memberId) {
        Member member = findMemberById(memberId);
        List<Member> family = memberRepository.findAllByFamilyId(member.getFamilyId());

        return family.stream()
                .map(Member::getId)
                .toList();
    }

    @Transactional
    public Page<FamilyMemberProfileResponse> findFamilyMembersProfilesByFamilyId(
            String familyId, int page, int size
    ) {
        Page<Member> memberPage = memberRepository.findAllByFamilyId(familyId, PageRequest.of(page - 1, size));
        List<Member> members = memberPage.getContent();

        List<FamilyMemberProfileResponse> familyMemberProfiles = createFamilyMemberProfiles(members);

        return new PageImpl<>(familyMemberProfiles, memberPage.getPageable(), memberPage.getTotalElements());
    }

    private List<FamilyMemberProfileResponse> createFamilyMemberProfiles(List<Member> members) {
        return members.stream()
                .map(FamilyMemberProfileResponse::of)
                .collect(Collectors.toList());
    }
}

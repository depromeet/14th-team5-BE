package com.oing.service;

import com.oing.domain.CreateNewUserDTO;
import com.oing.domain.SocialLoginProvider;
import com.oing.domain.model.Member;
import com.oing.domain.model.SocialMember;
import com.oing.domain.model.key.SocialMemberKey;
import com.oing.exception.MemberNotFoundException;
import com.oing.repository.MemberRepository;
import com.oing.repository.SocialMemberRepository;
import com.oing.util.IdentityGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public List<String> findFamilyMembersIdByMemberId(String memberId) {
        Member member = findMemberById(memberId);
        List<Member> family = memberRepository.findAllByFamilyId(member.getFamilyId());

        return family.stream()
                .map(Member::getId)
                .toList();
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
}

package com.oing.controller;

import com.oing.domain.exception.DomainException;
import com.oing.domain.exception.ErrorCode;
import com.oing.domain.model.Member;
import com.oing.dto.request.UpdateMemberRequest;
import com.oing.dto.response.FamilyMemberProfileResponse;
import com.oing.dto.response.FamilyMemberProfilesResponse;
import com.oing.dto.response.MemberResponse;
import com.oing.dto.response.PaginationResponse;
import com.oing.restapi.MemberApi;
import com.oing.service.MemberService;
import com.oing.util.AuthenticationHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController implements MemberApi {

    private final AuthenticationHolder authenticationHolder;
    private final MemberService memberService;

    @Override
    public FamilyMemberProfilesResponse getFamilyMemberProfileAndCreatedAt(Integer page, Integer size) {
        String userId = authenticationHolder.getUserId();
        PaginationResponse<FamilyMemberProfileResponse> familyMemberProfile = getFamilyMemberProfile(userId, page, size);
        String familyCreatedAt = memberService.findFamilyCreatedAt(userId);
        return new FamilyMemberProfilesResponse(familyMemberProfile, familyCreatedAt);
    }

    public PaginationResponse<FamilyMemberProfileResponse> getFamilyMemberProfile(
            String userId, Integer page, Integer size
    ) {
        List<String> familyMembersId = memberService.findFamilyMembersIdByMemberId(userId);
        List<FamilyMemberProfileResponse> responses = familyMembersId.stream()
                .map(this::mapToFamilyMemberProfileResponse)
                .toList();

        return new PaginationResponse<>(page, 3, size, false, responses);
    }

    private FamilyMemberProfileResponse mapToFamilyMemberProfileResponse(String memberId) {
        Member member = memberService.findMemberById(memberId);

        return new FamilyMemberProfileResponse(member.getId(), member.getName(), member.getProfileImgUrl());
    }

    @Override
    public MemberResponse getMember(String memberId) {
        Member member = memberService.findMemberById(memberId);

        return new MemberResponse(memberId, member.getName(), member.getProfileImgUrl());
    }

    @Override
    public MemberResponse updateMember(String memberId, UpdateMemberRequest request) {
        String memberIdBase = "01HGW2N7EHJVJ4CJ999RRS2E";
        memberId = "01HGW2N7EHJVJ4CJ999RRS2E";

        //TODO: 로그인한 사용자의 ID와 정보수정 대상 사용자의 ID가 같은지 확인
        if (memberIdBase.equals(memberId)) {
            //TODO: 프로필 이미지 및 닉네임 수정 로직 추가
            return new MemberResponse(
                    memberId,
                    request.name(),
                    request.profileImageUrl()
            );
        }
        throw new DomainException(ErrorCode.AUTHORIZATION_FAILED);
    }

    @Override
    public void deleteMember(String memberId) {
        String memberIdBase = "01HGW2N7EHJVJ4CJ999RRS2E97";
        memberId = "01HGW2N7EHJVJ4CJ999RRS2E97";

        //TODO: 탈퇴 요청한 회원 id와 요청으로 들어온 memberId 일치하는지 검증
        if (!memberIdBase.equals(memberId)) {
            throw new DomainException(ErrorCode.AUTHORIZATION_FAILED);
        }
        //TODO: 타객체들간의 연관관계 해제 및 마스킹 처리
    }
}

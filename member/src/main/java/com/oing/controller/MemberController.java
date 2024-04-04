package com.oing.controller;

import com.oing.domain.Member;
import com.oing.domain.PaginationDTO;
import com.oing.dto.request.PreSignedUrlRequest;
import com.oing.dto.request.QuitMemberRequest;
import com.oing.dto.request.UpdateMemberNameRequest;
import com.oing.dto.request.UpdateMemberProfileImageUrlRequest;
import com.oing.dto.response.*;
import com.oing.exception.AuthorizationFailedException;
import com.oing.restapi.MemberApi;
import com.oing.service.MemberService;
import com.oing.util.PreSignedUrlGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;

import static com.oing.exception.ErrorCode.UNAUTHORIZED_MEMBER_USED;

@Controller
@RequiredArgsConstructor
public class MemberController implements MemberApi {

    private final PreSignedUrlGenerator preSignedUrlGenerator;
    private final MemberService memberService;


    @Override
    public PaginationResponse<FamilyMemberProfileResponse> getFamilyMembersProfiles(
            Integer page, Integer size, String loginFamilyId
    ) {
        Page<FamilyMemberProfileResponse> profilePage = memberService.getFamilyMembersProfilesByFamilyId(
                loginFamilyId, page, size
        );

        PaginationDTO<FamilyMemberProfileResponse> paginationDTO = PaginationDTO.of(profilePage);
        return PaginationResponse.of(paginationDTO, page, size);
    }

    @Override
    public MemberResponse getMember(String memberId, String loginFamilyId) {
        validateFamilyMember(memberId, loginFamilyId);
        
        Member member = memberService.getMemberByMemberId(memberId);
        return MemberResponse.of(member);
    }

    @Override
    public PreSignedUrlResponse requestPresignedUrl(PreSignedUrlRequest request) {
        String imageName = request.imageName();
        return preSignedUrlGenerator.getProfileImagePreSignedUrl(imageName);
    }

    @Override
    public MemberResponse updateMemberProfileImageUrl(String memberId, UpdateMemberProfileImageUrlRequest request, String loginMemberId) {
        validateMemberIdMatch(memberId, loginMemberId);

        Member member = memberService.updateMemberProfileImageUrl(memberId, request.profileImageUrl());
        return MemberResponse.of(member);
    }

    @Override
    public MemberResponse deleteMemberProfileImageUrl(String memberId, String loginMemberId) {
        validateMemberIdMatch(memberId, loginMemberId);

        Member member = memberService.deleteMemberProfileImageUrl(memberId);
        return MemberResponse.of(member);
    }

    @Override
    public MemberResponse updateMemberName(String memberId, UpdateMemberNameRequest request, String loginMemberId) {
        validateMemberIdMatch(memberId, loginMemberId);

        Member member = memberService.updateMemberName(memberId, request.name());
        return MemberResponse.of(member);
    }

    @Override
    public DefaultResponse deleteMember(String memberId, QuitMemberRequest request, String loginMemberId) {
        validateMemberIdMatch(memberId, loginMemberId);

        memberService.deleteMember(memberId, request);
        return DefaultResponse.ok();
    }


    private void validateFamilyMember(String memberId, String loginFamilyId) {
        if (!memberService.isFamilyMember(memberId, loginFamilyId)) {
            throw new AuthorizationFailedException(UNAUTHORIZED_MEMBER_USED);
        }
    }

    private void validateMemberIdMatch(String memberId, String loginMemberId) {
        if (!loginMemberId.equals(memberId)) {
            throw new AuthorizationFailedException(UNAUTHORIZED_MEMBER_USED);
        }
    }
}

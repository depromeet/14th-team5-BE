package com.oing.controller;

import com.oing.domain.Member;
import com.oing.domain.PaginationDTO;
import com.oing.dto.request.PreSignedUrlRequest;
import com.oing.dto.request.QuitMemberRequest;
import com.oing.dto.request.UpdateMemberNameRequest;
import com.oing.dto.request.UpdateMemberProfileImageUrlRequest;
import com.oing.dto.response.*;
import com.oing.exception.UnauthorizedMemberException;
import com.oing.restapi.MemberApi;
import com.oing.service.MemberDeviceService;
import com.oing.service.MemberQuitReasonService;
import com.oing.service.MemberService;
import com.oing.util.PreSignedUrlGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MemberController implements MemberApi {

    private final PreSignedUrlGenerator preSignedUrlGenerator;
    private final MemberService memberService;
    private final MemberDeviceService memberDeviceService;
    private final MemberQuitReasonService memberQuitReasonService;

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
    @Transactional
    public MemberResponse updateMemberProfileImageUrl(String memberId, UpdateMemberProfileImageUrlRequest request, String loginMemberId) {
        validateMemberIdMatch(memberId, loginMemberId);
        
        Member member = memberService.getMemberByMemberId(memberId);
        String profileImgKey = preSignedUrlGenerator.extractImageKey(request.profileImageUrl());
        member.updateProfileImg(request.profileImageUrl(), profileImgKey);

        return MemberResponse.of(member);
    }

    @Override
    @Transactional
    public MemberResponse deleteMemberProfileImageUrl(String memberId, String loginMemberId) {
        validateMemberIdMatch(memberId, loginMemberId);

        Member member = memberService.getMemberByMemberId(memberId);
        member.deleteProfileImg();

        return MemberResponse.of(member);
    }

    @Override
    @Transactional
    public MemberResponse updateMemberName(String memberId, UpdateMemberNameRequest request, String loginMemberId) {
        validateMemberIdMatch(memberId, loginMemberId);

        Member member = memberService.getMemberByMemberId(memberId);
        member.updateName(request.name());

        return MemberResponse.of(member);
    }

    @Override
    @Transactional
    public DefaultResponse deleteMember(String memberId, QuitMemberRequest request, String loginMemberId) {
        validateMemberIdMatch(memberId, loginMemberId);

        Member member = memberService.getMemberByMemberId(memberId);
        memberService.deleteAllSocialMembersByMember(memberId);
        member.deleteMemberInfo();

        if (request != null) { //For Api Version Compatibility
            memberQuitReasonService.recordMemberQuitReason(memberId, request.reasonIds());
        }

        memberDeviceService.removeAllDevicesByMemberId(memberId);

        return DefaultResponse.ok();
    }


    private void validateFamilyMember(String memberId, String loginFamilyId) {
        if (!memberService.isFamilyMember(memberId, loginFamilyId)) {
            throw new UnauthorizedMemberException();
        }
    }

    private void validateMemberIdMatch(String memberId, String loginMemberId) {
        if (!loginMemberId.equals(memberId)) {
            throw new UnauthorizedMemberException();
        }
    }
}

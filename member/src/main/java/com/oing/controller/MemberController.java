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
import com.oing.service.MemberDeviceService;
import com.oing.service.MemberQuitReasonService;
import com.oing.service.MemberService;
import com.oing.service.PostBridge;
import com.oing.util.PreSignedUrlGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;

import java.security.InvalidParameterException;

@Controller
@RequiredArgsConstructor
public class MemberController implements MemberApi {

    private final PreSignedUrlGenerator preSignedUrlGenerator;
    private final PostBridge postBridge;
    private final MemberService memberService;
    private final MemberDeviceService memberDeviceService;
    private final MemberQuitReasonService memberQuitReasonService;

    @Override
    public PaginationResponse<FamilyMemberProfileResponse> getFamilyMembersProfiles(
            Integer page, Integer size, String loginFamilyId, String loginMemberId
    ) {
        Page<FamilyMemberProfileResponse> profilePage = memberService.findFamilyMembersProfilesByFamilyId(
                loginFamilyId, page, size
        );

        PaginationDTO<FamilyMemberProfileResponse> paginationDTO = PaginationDTO.of(profilePage);

        return PaginationResponse.of(paginationDTO, page, size);
    }

    @Override
    public MemberResponse getMember(String memberId, String loginFamilyId, String loginMemberId) {
        validateFamilyMember(memberId, loginFamilyId);
        Member member = memberService.findMemberById(memberId);

        return MemberResponse.of(member);
    }

    private void validateFamilyMember(String memberId, String loginFamilyId) {
        if (!memberService.findFamilyIdByMemberId(memberId).equals(loginFamilyId)) {
            throw new AuthorizationFailedException();
        }
    }

    @Override
    public PreSignedUrlResponse requestPresignedUrl(PreSignedUrlRequest request) {
        String imageName = request.imageName();
        return preSignedUrlGenerator.getProfileImagePreSignedUrl(imageName);
    }

    @Override
    @Transactional
    public MemberResponse updateMemberProfileImageUrl(String memberId, String loginMemberId,
                                                      UpdateMemberProfileImageUrlRequest request) {
        validateMemberId(memberId, loginMemberId);
        Member member = memberService.findMemberById(memberId);
        String profileImgKey = preSignedUrlGenerator.extractImageKey(request.profileImageUrl());
        member.updateProfileImg(request.profileImageUrl(), profileImgKey);

        return MemberResponse.of(member);
    }

    @Override
    @Transactional
    public MemberResponse deleteMemberProfileImageUrl(String memberId, String loginMemberId) {
        validateMemberId(memberId, loginMemberId);
        Member member = memberService.findMemberById(memberId);
        member.deleteProfileImg();

        return MemberResponse.of(member);
    }

    @Override
    @Transactional
    public MemberResponse updateMemberName(String memberId, String loginMemberId, UpdateMemberNameRequest request) {
        validateMemberId(memberId, loginMemberId);
        Member member = memberService.findMemberById(memberId);

        validateName(request.name());
        member.updateName(request.name());

        return MemberResponse.of(member);
    }

    private void validateName(String name) {
        if (name.length() < 1 || name.length() > 9) {
            throw new InvalidParameterException();
        }
    }

    @Override
    @Transactional
    public DefaultResponse deleteMember(String memberId, String loginMemberId, QuitMemberRequest request) {
        validateMemberId(memberId, loginMemberId);
        Member member = memberService.findMemberById(memberId);
        memberService.deleteAllSocialMembersByMember(memberId);
        member.deleteMemberInfo();

        if (request != null) { //For Api Version Compatibility
            memberQuitReasonService.recordMemberQuitReason(memberId, request.reasonIds());
        }

        memberDeviceService.removeAllDevicesByMemberId(memberId);

        return DefaultResponse.ok();
    }

    @Override
    public DefaultResponse pickMember(String memberId, String loginMemberId, String loginFamilyId) {
        if (postBridge.isUploadedToday(loginFamilyId, memberId)) {
            //TODO: 실패 로직
        }
        return DefaultResponse.ok();
    }

    private void validateMemberId(String memberId, String loginMemberId) {
        if (!loginMemberId.equals(memberId)) {
            throw new AuthorizationFailedException();
        }
    }
}

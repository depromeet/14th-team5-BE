package com.oing.controller;

import com.oing.domain.Member;
import com.oing.domain.PaginationDTO;
import com.oing.dto.request.PreSignedUrlRequest;
import com.oing.dto.request.UpdateMemberNameRequest;
import com.oing.dto.request.UpdateMemberProfileImageUrlRequest;
import com.oing.dto.response.*;
import com.oing.exception.AuthorizationFailedException;
import com.oing.restapi.MemberApi;
import com.oing.service.MemberService;
import com.oing.util.AuthenticationHolder;
import com.oing.util.PreSignedUrlGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;

import java.security.InvalidParameterException;

@Controller
@RequiredArgsConstructor
public class MemberController implements MemberApi {

    private final AuthenticationHolder authenticationHolder;
    private final PreSignedUrlGenerator preSignedUrlGenerator;
    private final MemberService memberService;

    @Override
    public PaginationResponse<FamilyMemberProfileResponse> getFamilyMembersProfiles(Integer page, Integer size) {
        String memberId = authenticationHolder.getUserId();
        String familyId = memberService.findFamilyIdByMemberId(memberId);
        Page<FamilyMemberProfileResponse> profilePage = memberService.findFamilyMembersProfilesByFamilyId(
                familyId, page, size
        );

        PaginationDTO<FamilyMemberProfileResponse> paginationDTO = PaginationDTO.of(profilePage);

        return PaginationResponse.of(paginationDTO, page, size);
    }

    @Override
    public MemberResponse getMember(String memberId) {
        Member member = memberService.findMemberById(memberId);

        return MemberResponse.of(member);
    }

    @Override
    public PreSignedUrlResponse requestPresignedUrl(PreSignedUrlRequest request) {
        String imageName = request.imageName();
        return preSignedUrlGenerator.getProfileImagePreSignedUrl(imageName);
    }

    @Override
    @Transactional
    public MemberResponse updateMemberProfileImageUrl(String memberId, UpdateMemberProfileImageUrlRequest request) {
        validateMemberId(memberId);
        Member member = memberService.findMemberById(memberId);
        String profileImgKey = preSignedUrlGenerator.extractImageKey(request.profileImageUrl());
        member.updateProfileImg(request.profileImageUrl(), profileImgKey);

        return MemberResponse.of(member);
    }

    @Override
    @Transactional
    public MemberResponse updateMemberName(String memberId, UpdateMemberNameRequest request) {
        validateMemberId(memberId);
        Member member = memberService.findMemberById(memberId);

        validateName(request.name());
        member.updateName(request.name());

        return MemberResponse.of(member);
    }

    private void validateName(String name) {
        if (name.length() < 2 || name.length() > 10) {
            throw new InvalidParameterException();
        }
    }

    @Override
    @Transactional
    public DefaultResponse deleteMember(String memberId) {
        validateMemberId(memberId);
        Member member = memberService.findMemberById(memberId);
        memberService.deleteAllSocialMembersByMember(memberId);
        member.deleteMemberInfo();

        return DefaultResponse.ok();
    }

    private void validateMemberId(String memberId) {
        String loginId = authenticationHolder.getUserId();
        if (!loginId.equals(memberId)) {
            throw new AuthorizationFailedException();
        }
    }
}

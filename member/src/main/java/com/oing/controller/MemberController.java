package com.oing.controller;

import com.oing.domain.PaginationDTO;
import com.oing.domain.exception.DomainException;
import com.oing.domain.exception.ErrorCode;
import com.oing.domain.model.Member;
import com.oing.dto.request.PreSignedUrlRequest;
import com.oing.dto.request.UpdateMemberNameRequest;
import com.oing.dto.request.UpdateMemberProfileImageUrlRequest;
import com.oing.dto.response.FamilyMemberProfileResponse;
import com.oing.dto.response.MemberResponse;
import com.oing.dto.response.PaginationResponse;
import com.oing.dto.response.PreSignedUrlResponse;
import com.oing.restapi.MemberApi;
import com.oing.service.MemberService;
import com.oing.util.AuthenticationHolder;
import com.oing.util.PreSignedUrlGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;

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

        return new MemberResponse(memberId, member.getName(), member.getProfileImgUrl());
    }

    @Override
    public PreSignedUrlResponse requestPresignedUrl(PreSignedUrlRequest request) {
        String imageName = request.imageName();
        return preSignedUrlGenerator.getProfileImagePreSignedUrl(imageName);
    }

    @Override
    @Transactional
    public MemberResponse updateMemberProfileImageUrl(UpdateMemberProfileImageUrlRequest request) {
        String memberId = authenticationHolder.getUserId();
        Member member = memberService.findMemberById(memberId);
        //deleteMemberProfileImage(member.getProfileImgUrl());
        member.updateProfileImgUrl(request.profileImageUrl());

        return new MemberResponse(member.getId(), member.getName(), member.getProfileImgUrl());
    }

    private void deleteMemberProfileImage(String profileImageUrl) {
        if (profileImageUrl != null) {
            preSignedUrlGenerator.deleteImageByPath(profileImageUrl);
        }
    }

    @Override
    @Transactional
    public MemberResponse updateMemberName(UpdateMemberNameRequest request) {
        String memberId = authenticationHolder.getUserId();
        Member member = memberService.findMemberById(memberId);

        validateName(request.name());
        member.updateName(request.name());

        return new MemberResponse(member.getId(), member.getName(), member.getProfileImgUrl());
    }

    private void validateName(String name) {
        if (name.length()<2 || name.length()>10) {
            throw new DomainException(ErrorCode.INVALID_INPUT_VALUE);
        }
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

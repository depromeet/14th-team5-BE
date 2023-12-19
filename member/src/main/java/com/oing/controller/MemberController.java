package com.oing.controller;

import com.oing.domain.exception.DomainException;
import com.oing.domain.exception.ErrorCode;
import com.oing.domain.model.Member;
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
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController implements MemberApi {

    private final AuthenticationHolder authenticationHolder;
    private final PreSignedUrlGenerator preSignedUrlGenerator;
    private final MemberService memberService;

    @Override
    public PaginationResponse<FamilyMemberProfileResponse> getFamilyMemberProfile(Integer page, Integer size) {
        String memberIdBase = "01HGW2N7EHJVJ4CJ999RRS2E";
        String memberNameBase = "디프만";

        List<FamilyMemberProfileResponse> mockResponses = new ArrayList<>();
        for(int i = 0; i < size; i++) {
            int currentIndex = i + ((page - 1) * size);
            String suffix = String.format("%02d", currentIndex);
            mockResponses.add(
                    new FamilyMemberProfileResponse(
                            memberIdBase + suffix,
                            memberNameBase + suffix,
                            "https://picsum.photos/200/300?random=" + currentIndex
                    )
            );
        }

        return new PaginationResponse<>(page, 3, size, false, mockResponses);
    }

    @Override
    public MemberResponse getMember(String memberId) {
        String memberIdBase = "01HGW2N7EHJVJ4CJ999RRS2E";
        String memberNameBase = "디프만";

        return new MemberResponse(
                memberIdBase,
                memberNameBase,
                "https://picsum.photos/200/300?random=1"
        );
    }

    @Override
    public PreSignedUrlResponse requestPresignedUrl(String imageName) {
        return preSignedUrlGenerator.getProfileImagePreSignedUrl(imageName);
    }

    @Override
    @Transactional
    public MemberResponse updateMemberProfileImageUrl(UpdateMemberProfileImageUrlRequest request) {
        String memberId = authenticationHolder.getUserId();
        Member member = memberService.findMemberById(memberId);

        member.updateProfileImgUrl(request.profileImageUrl());

        return new MemberResponse(member.getId(), member.getName(), member.getProfileImgUrl());
    }

    @Override
    @Transactional
    public MemberResponse updateMemberName(UpdateMemberNameRequest request) {
        String memberId = authenticationHolder.getUserId();
        Member member = memberService.findMemberById(memberId);

        member.updateName(request.name());

        return new MemberResponse(member.getId(), member.getName(), member.getProfileImgUrl());
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

package com.oing.controller;

import com.oing.domain.exception.DomainException;
import com.oing.domain.exception.ErrorCode;
import com.oing.dto.request.UpdateMemberRequest;
import com.oing.dto.response.FamilyMemberProfileResponse;
import com.oing.dto.response.MemberResponse;
import com.oing.dto.response.PaginationResponse;
import com.oing.restapi.MemberApi;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MemberController implements MemberApi {

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
    public MemberResponse updateMember(String memberId, UpdateMemberRequest request) {
        //TODO: 수정 요청한 회원 id와 요청으로 들어온 memberId 일치하는지 검증
        return null;
    }

    @Override
    public MemberResponse deleteMember(String memberId) {
        String memberIdBase = "01HGW2N7EHJVJ4CJ999RRS2E97";
        memberId = "01HGW2N7EHJVJ4CJ999RRS2E97";
        //TODO: 회원 탈퇴 사유 저장

        //TODO: 탈퇴 요청한 회원 id와 요청으로 들어온 memberId 일치하는지 검증
        if (memberIdBase.equals(memberId)) {
            //TODO: 타객체들간의 연관관계 해제 및 마스킹 처리
            return new MemberResponse(
                    memberIdBase,
                    null,
                    null
            );
        }
        throw new DomainException(ErrorCode.AUTHORIZATION_FAILED);
    }
}

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
}

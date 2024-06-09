package com.oing.restapi;

import com.oing.dto.response.FamilyInviteDeepLinkResponse;
import com.oing.util.security.LoginMemberId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "가족초대 페이지 뷰 기반 API", description = "웹뷰와 앱에서 가족초대와 관련된 페이지 기반으로 작성된 View API입니다.")
@RestController
@RequestMapping("/v1/view/family-invite")
public interface FamilyInviteViewApi {

    @Operation(
            summary = "가족 초대 링크 정보 조회 (딥링크 웹뷰 + 가족 가입 앱 겸용)",
            description = """
                    linkId를 통해 해당 가족 초대 링크의 세부 정보들을 조회합니다.
                    토큰 미첨부 시 : 가족 초대 링크 상세 정보 조회 (딥링크 웹뷰 페이지용)
                    토큰 첨부 시 : 요청자 입장에서의 가족 초대 링크 정보 조회 (가족 가입 프로세스 앱 페이지용)"""
    )
    @GetMapping("/{linkId}")
    FamilyInviteDeepLinkResponse getFamilyInviteLinkDetails(
            @PathVariable
            @Parameter(description = "링크 ID", example = "bef039df")
            String linkId,

            @LoginMemberId
            @Parameter(hidden = true)
            String loginMemberId
    );
}

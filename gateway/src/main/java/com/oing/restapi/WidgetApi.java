package com.oing.restapi;

import com.oing.dto.response.SingleRecentPostWidgetResponse;
import com.oing.util.security.LoginFamilyId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "위젯 API", description = "위젯 관련 API")
@RestController
@Valid
@RequestMapping("/v1/widgets")
public interface WidgetApi {

    @Operation(summary = "당일 최근 가족 게시물 타입 위젯 조회", description = "당일 가장 최근에 올라온 가족의 게시글 하나를 표현하는 위젯을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @ApiResponse(responseCode = "204", description = "조회 성공했지만, 아직 업로드 된 게시글이 없음.")
    @GetMapping("/single-recent-family-post")
    ResponseEntity<SingleRecentPostWidgetResponse> getSingleRecentFamilyPostWidget(
            @RequestParam(required = false)
            @Parameter(description = "조회할 년월일 (default = today)", example = "2023-10-18")
            String date,

            @Parameter(hidden = true)
            @LoginFamilyId
            String loginFamilyId
    );
}

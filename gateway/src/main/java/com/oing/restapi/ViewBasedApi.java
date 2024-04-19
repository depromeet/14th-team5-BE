package com.oing.restapi;

import com.oing.dto.response.MainPageResponse;
import com.oing.util.security.LoginMemberId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 4/16/24
 * Time: 4:01 PM
 */
@Tag(name = "뷰 기반 API")
@RestController
@RequestMapping("/v1/view")
public interface ViewBasedApi {
    @Operation(summary = "메인 페이지 조회")
    @GetMapping("/main")
    MainPageResponse getMainPage(
            @Parameter(hidden = true)
            @LoginMemberId
            String loginMemberId
    );
}

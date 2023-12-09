package com.oing.restapi;

import com.oing.dto.response.MemberResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 12/7/23
 * Time: 6:01 PM
 */
@Tag(name = "멤버 API", description = "멤버(사용자) 관련 API")
@RestController
@RequestMapping("/v1/members")
public interface MemberApi {
    @GetMapping("/{memberId}")
    MemberResponse getMember(@PathVariable String memberId);
}

package com.oing.restapi;

import com.oing.dto.response.NotificationResponse;
import com.oing.util.security.LoginMemberId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "알림 조회 API", description = "알림 조회 관련 API")
@RestController
@RequestMapping("/v1/notifications")
public interface NotificationApi {
    @Operation(summary = "현재 보여져야할 알림 조회")
    @GetMapping
    List<NotificationResponse> getNotifications(
            @Parameter(hidden = true)
            @LoginMemberId
            String loginMemberId
    );
}

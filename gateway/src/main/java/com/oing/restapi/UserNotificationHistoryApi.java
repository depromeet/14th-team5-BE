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

@Tag(name = "유저 알림 이력 API", description = "유저의 알림 이력 관련 API")
@RestController
@RequestMapping("/v1/notifications")
public interface UserNotificationHistoryApi {

    @Operation(summary = "현재 보여져야할 알림 조회", description = "로그인 된 유저의 최근 한 달 동안의 알림 이력을 조회한다.")
    @GetMapping
    List<NotificationResponse> getMyRecentNotifications(
            @Parameter(hidden = true)
            @LoginMemberId
            String loginMemberId
    );
}

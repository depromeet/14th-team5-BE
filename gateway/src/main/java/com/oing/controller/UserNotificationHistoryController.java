package com.oing.controller;

import com.oing.domain.ProfileStyle;
import com.oing.dto.response.NotificationResponse;
import com.oing.restapi.UserNotificationHistoryApi;
import com.oing.service.UserNotificationHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.time.ZonedDateTime;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class UserNotificationHistoryController implements UserNotificationHistoryApi {

    private final UserNotificationHistoryService userNotificationHistoryService;

    @Override
    public List<NotificationResponse> getMyRecentNotifications(String loginMemberId) {
        return userNotificationHistoryService.getRecentUserNotifications(loginMemberId);
    }
}

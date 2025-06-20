package com.oing.controller;

import com.oing.dto.response.NotificationResponse;
import com.oing.restapi.MemberNotificationHistoryApi;
import com.oing.service.MemberNotificationHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class MemberNotificationHistoryController implements MemberNotificationHistoryApi {

    private final MemberNotificationHistoryService memberNotificationHistoryService;

    @Override
    public List<NotificationResponse> getMyRecentNotifications(String loginMemberId, int page, int size) {
        return memberNotificationHistoryService.getRecentUserNotifications(loginMemberId, page, size);
    }
}

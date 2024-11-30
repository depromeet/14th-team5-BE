package com.oing.controller;

import com.oing.dto.response.NotificationResponse;
import com.oing.restapi.NotificationApi;
import org.springframework.stereotype.Controller;

import java.time.ZonedDateTime;
import java.util.List;

@Controller
public class NotificationController implements NotificationApi {
    @Override
    public List<NotificationResponse> getNotifications(String loginMemberId) {
        return List.of(
                new NotificationResponse(
                        "01HGW2N7EHJVJ4CJ999RRS2E97",
                        "https://..",
                        "우리 가족 모두가 생존신고를 완료했어요",
                        "우리 가족 모두가 생존신고를 완료했어요",
                        ZonedDateTime.now()
                )
        );
    }
}

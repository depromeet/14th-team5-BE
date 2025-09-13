package com.oing.event;

import com.oing.component.SlackGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.TimeZone;

@Component
@RequiredArgsConstructor
public class MissionEventListener {
    private final SlackGateway slackGateway;

    @EventListener
    public void onMissionRegistered(DailyMissionRegisteredEvent event) {
        SlackGateway.SlackBotDto dto = SlackGateway.SlackBotDto.builder()
                .attachments(List.of(
                        SlackGateway.SlackBotAttachmentDto.builder()
                                .authorName("삐삐 백엔드 서버")
                                .title("미션 등록 알림")
                                .color("#66ff00")
                                .text("오늘자 미션이 등록되었어요.")
                                .fields(List.of(
                                        SlackGateway.SlackBotFieldDto.builder()
                                                .title("오늘 미션 내용")
                                                .value(event.getMission().getContent())
                                                .shortField(false)
                                                .build(),
                                        SlackGateway.SlackBotFieldDto.builder()
                                                .title("에러 유무")
                                                .value((event.getThrowable() == null) ? "없음" : "있음")
                                                .shortField(false)
                                                .build(),
                                        SlackGateway.SlackBotFieldDto.builder()
                                                .title("에러 내용")
                                                .value((event.getThrowable() == null) ? "없음" : event.getThrowable().getMessage())
                                                .shortField(false)
                                                .build()
                                ))
                                .build()
                ))
                .build();
        slackGateway.sendNotificationSlack(dto);
    }
}

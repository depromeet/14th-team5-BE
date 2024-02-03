package com.oing.component;

import com.oing.domain.BulkNotificationCompletedEvent;
import com.oing.domain.ErrorReportDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/11/27
 * Time: 1:16 PM
 */
@RequiredArgsConstructor
@Component
public class ApplicationListener {
    private final SlackGateway slackGateway;
    private final Environment environment;

    private boolean isProductionInstance() {
        String[] activeProfiles = environment.getActiveProfiles();
        return activeProfiles != null && (Arrays.asList(activeProfiles).contains("prod") || Arrays.asList(activeProfiles).contains("dev"));
    }

    @Async
    @EventListener
    public void onNotificationSent(BulkNotificationCompletedEvent event) {
        if (!isProductionInstance()) return;
        SlackGateway.SlackBotDto dto = SlackGateway.SlackBotDto.builder()
                .attachments(List.of(
                        SlackGateway.SlackBotAttachmentDto.builder()
                                .authorName("no5ing-server")
                                .title("알림 발송 리포트")
                                .color("#abcdef")
                                .text("백엔드 서버에서 FCM 벌크 알림이 발송되었습니다")
                                .fields(List.of(
                                        SlackGateway.SlackBotFieldDto.builder()
                                                .title("알림 종류")
                                                .value(event.reason())
                                                .shortField(false)
                                                .build(),
                                        SlackGateway.SlackBotFieldDto.builder()
                                                .title("알람 발송 대상 기기 수")
                                                .value(String.valueOf(event.totalTargets()))
                                                .shortField(true)
                                                .build(),
                                        SlackGateway.SlackBotFieldDto.builder()
                                                .title("알람 발송 대상 사용자 수")
                                                .value(String.valueOf(event.totalMembers()))
                                                .shortField(true)
                                                .build(),
                                        SlackGateway.SlackBotFieldDto.builder()
                                                .title("소요시간 (밀리초)")
                                                .value(String.valueOf(event.elapsedMillis()))
                                                .shortField(true)
                                                .build()
                                ))
                                .build()
                ))
                .build();

        slackGateway.sendSlackBotMessage(dto);
    }

    @Async
    @EventListener
    public void onErrorReport(ErrorReportDTO errorReportDTO) {
        if (!isProductionInstance()) return;

        String errorMessage = errorReportDTO.errorMessage() == null ? "알 수 없는 에러" : errorReportDTO.errorMessage();
        SlackGateway.SlackBotDto dto = SlackGateway.SlackBotDto.builder()
                .attachments(List.of(
                        SlackGateway.SlackBotAttachmentDto.builder()
                                .authorName("no5ing-server")
                                .title("백엔드 서비스 에러 리포트")
                                .text("백엔드 서버에서 핸들링되지 않은 오류가 발생하였습니다")
                                .fields(List.of(
                                        SlackGateway.SlackBotFieldDto.builder()
                                                .title("에러 메시지")
                                                .value(errorMessage)
                                                .shortField(false)
                                                .build(),
                                        SlackGateway.SlackBotFieldDto.builder()
                                                .title("에러 페이로드")
                                                .value(errorReportDTO.payload())
                                                .shortField(false)
                                                .build()
                                ))
                                .build()
                ))
                .build();

        slackGateway.sendSlackBotMessage(dto);
    }
}

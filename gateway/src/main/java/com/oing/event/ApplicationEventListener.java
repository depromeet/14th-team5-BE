package com.oing.event;

import com.oing.component.SlackGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

@RequiredArgsConstructor
@Component
public class ApplicationEventListener implements ApplicationListener<ApplicationReadyEvent> {
    private final SlackGateway slackGateway;
    private final Environment environment;
    private final BuildProperties buildProperties;

    private boolean isProductionInstance() {
        String[] activeProfiles = environment.getActiveProfiles();
        return activeProfiles != null && (Arrays.asList(activeProfiles).contains("prod") || Arrays.asList(activeProfiles).contains("dev"));
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (!isProductionInstance()) return;

        String profileName = environment.getActiveProfiles()[0];
        ZonedDateTime buildTime = buildProperties.getTime().atZone(ZoneId.of("Asia/Seoul"));

        SlackGateway.SlackBotDto dto = SlackGateway.SlackBotDto.builder()
                .attachments(List.of(
                        SlackGateway.SlackBotAttachmentDto.builder()
                                .authorName("삐삐 백엔드 서버")
                                .title(profileName + " 서버 부트스트랩 알림")
                                .color("#66ff00")
                                .text("백엔드 스프링부트가 준비되었습니다.")
                                .fields(List.of(
                                        SlackGateway.SlackBotFieldDto.builder()
                                                .title("현재 서버 타임존")
                                                .value(TimeZone.getDefault().toString())
                                                .shortField(true)
                                                .build(),
                                        SlackGateway.SlackBotFieldDto.builder()
                                                .title("부트스트랩 소요 시간")
                                                .value(event.getTimeTaken().toSeconds() + " 초")
                                                .shortField(true)
                                                .build(),
                                        SlackGateway.SlackBotFieldDto.builder()
                                                .title("현재 서버 시간")
                                                .value(LocalDateTime.now().toString())
                                                .shortField(false)
                                                .build(),
                                        SlackGateway.SlackBotFieldDto.builder()
                                                .title("해당 이미지 빌드 시점")
                                                .value(buildTime.toString())
                                                .shortField(false)
                                                .build()
                                ))
                                .build()
                ))
                .build();
        slackGateway.sendNotificationSlack(dto);
    }
}

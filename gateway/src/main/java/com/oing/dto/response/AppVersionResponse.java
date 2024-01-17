package com.oing.dto.response;

import com.oing.domain.AppVersion;
import com.oing.domain.DeepLinkType;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@Schema(description = "앱 버전 응답")
public class AppVersionResponse {
    @Parameter(description = "앱 키", example = "5a80edc0-5b7e-4b7e-9b7e-5b7e4b7e9b7e")
    private String appKey;

    @Parameter(description = "앱 버전", example = "1.0.0")
    private String appVersion;

    @Parameter(description = "현재 서비스 유무", example = "true")
    private boolean isInService;

    @Parameter(description = "현재 심사중 유무", example = "true")
    private boolean isInReview;

    @Parameter(description = "현재 최신버전 유무", example = "true")
    private boolean isLatest;

    public static AppVersionResponse from(AppVersion appVersion) {
        return new AppVersionResponse(
                appVersion.getAppKey().toString(),
                appVersion.getAppVersion(),
                appVersion.isInService(),
                appVersion.isInReview(),
                appVersion.isLatest()
        );
    }
}

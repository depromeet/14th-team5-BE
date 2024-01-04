package com.oing.component;

import com.oing.domain.AppVersion;
import com.oing.repository.AppVersionRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 1/4/24
 * Time: 11:52 AM
 */
@RequiredArgsConstructor
@Component
public class AppVersionCache {
    private static final Map<UUID, AppVersion> appVersionMap = new HashMap<>();
    private final AppVersionRepository appVersionRepository;

    @PostConstruct
    private void onBeanInitialize() {
        refreshAppVersion();
    }

    @Scheduled(initialDelay = 1000 * 30L, fixedDelay = 1000 * 30L)
    private void refreshAppVersion() {
        for (AppVersion appVersion : appVersionRepository.findAll()) {
            appVersionMap.put(appVersion.getAppKey(), appVersion);
        }
    }

    public boolean isServiceable(UUID appKey) {
        AppVersion appVersion = appVersionMap.get(appKey);
        return appVersion != null && appVersion.isInService();
    }
}

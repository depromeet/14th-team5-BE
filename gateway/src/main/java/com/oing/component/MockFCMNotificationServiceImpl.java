package com.oing.component;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.oing.config.FirebaseConfig;
import com.oing.service.FCMNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2/2/24
 * Time: 4:10 AM
 */

@Slf4j
@RequiredArgsConstructor
@Component
@ConditionalOnMissingBean(FirebaseConfig.class)
public class MockFCMNotificationServiceImpl implements FCMNotificationService {

    @Override
    public void sendMessage(Message message) {
        log.info("MockFCMNotificationServiceImpl.sendMessage: {}", message);
    }

    @Override
    public void sendMulticastMessage(MulticastMessage message) {
        log.info("MockFCMNotificationServiceImpl.sendMulticastMessage: {}", message);
    }
}

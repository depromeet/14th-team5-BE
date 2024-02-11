package com.oing.component;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.oing.config.FirebaseConfig;
import com.oing.service.FCMNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2/2/24
 * Time: 4:10 AM
 */

@RequiredArgsConstructor
@Component
@ConditionalOnBean(FirebaseConfig.class)
public class FCMNotificationServiceImpl implements FCMNotificationService {
    private final FirebaseMessaging firebaseMessaging;

    @Override
    public void sendMessage(Message message) {
        firebaseMessaging.sendAsync(message);
    }

    @Override
    public void sendMulticastMessage(MulticastMessage message) {
        firebaseMessaging.sendEachForMulticastAsync(message);
    }
}

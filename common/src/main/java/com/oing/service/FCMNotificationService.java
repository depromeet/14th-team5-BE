package com.oing.service;

import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2/2/24
 * Time: 4:09 AM
 */
public interface FCMNotificationService {
    void sendMessage(Message message);
    void sendMulticastMessage(MulticastMessage message);
}

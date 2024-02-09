package com.oing.util;

import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.Aps;
import com.google.firebase.messaging.Notification;

public class FCMNotificationUtil {
    public static Notification buildNotification(String title, String body){
        return Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();
    }

    public static ApnsConfig buildApnsConfig(){
        return ApnsConfig.builder()
                .setAps(Aps.builder().setSound("default").build())
                .build();
    }

    public static AndroidConfig buildAndroidConfig() {
        return AndroidConfig.builder()
                .setPriority(AndroidConfig.Priority.HIGH)
                .build();
    }

}

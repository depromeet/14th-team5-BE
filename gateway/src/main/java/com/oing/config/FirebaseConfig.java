package com.oing.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2/2/24
 * Time: 3:59 AM
 */
@Profile({"prod", "dev"})
@Configuration
public class FirebaseConfig {
    @Value("${cloud.firebase}")
    private String firebaseSecret;

    @Bean
    public FirebaseMessaging firebaseMessaging() throws IOException {
        ByteArrayInputStream credentials = new ByteArrayInputStream(Base64.getDecoder().decode(firebaseSecret));
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(credentials)).build();
        FirebaseApp firebaseApp = FirebaseApp.initializeApp(options);
        return FirebaseMessaging.getInstance(firebaseApp);
    }
}

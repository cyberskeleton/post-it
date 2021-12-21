package com.example.postitserver.configurations;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;


@Configuration
public class FirebaseMessagingConfig {

    /**
     * returns configured Firebase communication bean
     *
     * @return Firebase SDK instance
     * @throws IOException if exception occurs
     */
    @Bean
    FirebaseMessaging firebaseMessaging() throws IOException {
        final GoogleCredentials googleCredentials = GoogleCredentials
            .fromStream(new ClassPathResource("google/firebase-service-account.json").getInputStream());
        final FirebaseOptions firebaseOptions = FirebaseOptions.builder()
            .setCredentials(googleCredentials)
            .build();
        final FirebaseApp app = FirebaseApp.initializeApp(firebaseOptions, "post-it-notes-program");

        return FirebaseMessaging.getInstance(app);
    }
}

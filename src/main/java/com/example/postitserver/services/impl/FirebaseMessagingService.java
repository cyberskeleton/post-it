package com.example.postitserver.services.impl;

import com.example.postitserver.models.Note;
import com.example.postitserver.models.NoteDTO;
import com.example.postitserver.services.NoteService;
import com.example.postitserver.services.NotificationService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.TopicManagementResponse;
import com.google.firebase.messaging.WebpushConfig;
import com.google.firebase.messaging.WebpushNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class FirebaseMessagingService implements NotificationService {
    private static final Logger LOG = LoggerFactory.getLogger(FirebaseMessagingService.class);

    private final FirebaseMessaging firebaseMessaging;
    private final NoteService noteService;

    public FirebaseMessagingService(final FirebaseMessaging firebaseMessaging, final NoteService noteService) {
        this.firebaseMessaging = firebaseMessaging;
        this.noteService = noteService;
    }

    /**
     * Firebase sends notification to device by token
     * @param noteDTO note Data Transfer Object
     * @return result of operation
     * @throws FirebaseMessagingException if error occurs while using Firebase SDK
     */
    @Override
    public String sendByToken(final NoteDTO noteDTO) throws FirebaseMessagingException {
        final Notification notification = Notification.builder()
            .setTitle(noteDTO.subject())
            .setBody(noteDTO.content())
            .build();
        final Message message = Message.builder()
            .setToken(noteDTO.token())
            .setNotification(notification)
            .build();

        return this.firebaseMessaging.send(message);
    }

    /**
     * Firebase sends notification to topic
     * @param noteDTO note Data Transfer Object
     * @param topic name of topic of notifications
     * @return result of send operation
     * @throws FirebaseMessagingException if error occurs while using Firebase SDK
     */
    @Override
    public String sendToTopic(final NoteDTO noteDTO, final String topic) throws FirebaseMessagingException {
        final WebpushNotification notification = WebpushNotification.builder()
            .setTitle(noteDTO.subject())
            .setBody(noteDTO.content())
            .build();
        final WebpushConfig config = WebpushConfig.builder()
            .putHeader("ttl", "300")
            .setNotification(notification)
            .build();
        final Message message = Message.builder()
            .putAllData(noteDTO.data())
            .setTopic(topic)
            .setWebpushConfig(config)
            .build();

        return this.firebaseMessaging.send(message);
    }

    /**
     * Firebase adds devices by tokens to topic subscribers
     * @param tokens device tokens to send notifications to
     * @param topic name of topic of notifications
     * @return numbers of successful subscriptions, null if error occurs while using Firebase SDK
     */
    @Override
    public Integer subscribeToTopic(final List<String> tokens, final String topic) {
        try {
            final TopicManagementResponse response = this.firebaseMessaging.subscribeToTopic(tokens, topic);
            LOG.info("Push Message sent: {}", response.getSuccessCount() + " tokens have been successfully subscribed");

            return response.getSuccessCount();
        }
        catch (FirebaseMessagingException e) {
            LOG.error("Error occurred: {}", e.getMessage());

            return null;
        }
    }

    /**
     * sends notification
     */
    @Scheduled(fixedDelay = 60_000)
    public void sendNotificationMessages() {
        LOG.info("Scheduled");
        final Instant after = Instant.now();
        final Instant before = after.plusSeconds(60L);
        final List<Note> notes = this.noteService.findByTimeBetween(after, before);

        for (final Note note : notes) {
            LOG.info("Note to send: {}", note);
            try {
                final NoteDTO noteDTO = new NoteDTO(note.getSubject(), note.getContent(), null, note.getTime(), note.getToken());
                final String response = this.sendByToken(noteDTO);
                LOG.info("Push Message sent: {}", response);
            } catch (FirebaseMessagingException e) {
                LOG.error("Error occurred: {}", e.getMessage());
            }
        }
    }
}

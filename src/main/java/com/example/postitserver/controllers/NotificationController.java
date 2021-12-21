package com.example.postitserver.controllers;

import com.example.postitserver.models.NoteDTO;
import com.example.postitserver.services.NotificationService;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/v1/notifications")
@RestController
public class NotificationController {
    private static final Logger LOG = LoggerFactory.getLogger(NotificationController.class);

    private final NotificationService notificationService;

    public NotificationController(final NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * sends notification by device token
     * @param noteDTO note Data Transfer Object
     * @return 200 if successful, 500 if error occurs
     * @throws FirebaseMessagingException if error occurs while using Firebase SDK
     */
    @PostMapping("/tokens")
    public ResponseEntity<String> sendNotificationByToken(@RequestBody NoteDTO noteDTO) throws FirebaseMessagingException {
        LOG.info("Request to send a Notification by token: {}", noteDTO);

        final String responseBody = notificationService.sendByToken(noteDTO);
        if (responseBody == null) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok("Message has been successfully sent to: " + responseBody);
    }

    /**
     * sends notification to topic
     * @param noteDTO note Data Transfer Object
     * @param topic name of topic of notifications
     * @return 200 if successful, 500 if error occurs
     * @throws FirebaseMessagingException if error occurs while using Firebase SDK
     */
    @PostMapping("/topics/{topic}")
    public ResponseEntity<String> sendNotificationToTopic(@RequestBody NoteDTO noteDTO, @PathVariable(value = "topic")  String topic)
            throws FirebaseMessagingException {
        LOG.info("Request to add a Notification to topic: {}", topic);

        final String responseBody = notificationService.sendToTopic(noteDTO, topic);
        if (responseBody == null) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok("Message has been successfully sent to: " + responseBody);
    }

    /**
     * adds device by token to topic subscribers
     * @param request contains token and topic name
     * @return 200 if successful, 500 if error occurs while using Firebase SDK
     */
    @PostMapping("/subscriptions")
    public ResponseEntity<String> subscribeToTopic(@RequestBody SubscriptionRequest request) {
        LOG.info("Request to add Notifications to topic: {}", request.topic);

        final Integer responseBody = notificationService.subscribeToTopic(request.tokens, request.topic);
        if (responseBody == null) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok(responseBody + " tokens have been successfully subscribed to the topic: " + request.topic);
    }

    record SubscriptionRequest(List<String> tokens, String topic){}
}

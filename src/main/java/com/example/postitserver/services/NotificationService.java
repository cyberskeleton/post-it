package com.example.postitserver.services;

import com.example.postitserver.models.NoteDTO;
import com.google.firebase.messaging.FirebaseMessagingException;

import java.util.List;

public interface NotificationService {
    /**
     * sends notification by device token
     * @param noteDTO note Data Transfer Object
     * @return 200 if successful, 500 if error occurs
     * @throws FirebaseMessagingException if error occurs while using Firebase SDK
     */
    String sendByToken(NoteDTO noteDTO) throws FirebaseMessagingException;

    /**
     * sends notification to topic
     * @param noteDTO note Data Transfer Object
     * @param topic name of topic of notifications
     * @return 200 if successful, 500 if error occurs
     * @throws FirebaseMessagingException if error occurs while using Firebase SDK
     */
    String sendToTopic(NoteDTO noteDTO, String topic) throws FirebaseMessagingException;

    /**
     * adds device by token to topic subscribers
     * @param tokens contains list of tokens to subscribe to topic
     * @param topic contains topic name
     * @return 200 if successful, 500 if error occurs while using Firebase SDK
     */
    Integer subscribeToTopic(List<String> tokens, String topic);
}

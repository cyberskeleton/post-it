package com.example.postitserver.services.impl;

import com.example.postitserver.models.NoteDTO;
import com.example.postitserver.services.NoteService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.TopicManagementResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FirebaseMessagingServiceTest {

    private static final NoteDTO TEST_DTO = new NoteDTO("test", "test", null,Instant.ofEpochSecond(5000), "test");

    private FirebaseMessaging firebaseMessaging;
    private NoteService noteService;
    private FirebaseMessagingService service;

    @BeforeEach
    void setUp() {
        firebaseMessaging = mock(FirebaseMessaging.class);
        noteService = mock(NoteService.class);
        service = new FirebaseMessagingService(firebaseMessaging, noteService);
    }

    @Test
    void sendByToken() throws Exception {
        when(firebaseMessaging.send(any())).thenReturn("test");

        final String result = service.sendByToken(TEST_DTO);

        assertThat(result).isEqualTo("test");
    }

    @Test
    void sendByToken_shouldThrowException() throws Exception {
        when(firebaseMessaging.send(any())).thenThrow(new RuntimeException("test", new Throwable()));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.sendByToken(TEST_DTO));

        assertThat(exception).hasMessage("test");
    }

    @Test
    void sendToTopic() throws Exception {
        final NoteDTO noteDTO = new NoteDTO("test", "test", new HashMap<>(), Instant.ofEpochSecond(5000), "test");
        when(firebaseMessaging.send(any())).thenReturn("test");

        final String result = service.sendToTopic(noteDTO, "test");

        assertThat(result).isEqualTo("test");
    }

    @Test
    void sendToTopic_shouldThrowException() throws Exception {
        final NoteDTO noteDTO = new NoteDTO("test", "test", new HashMap<>(), Instant.ofEpochSecond(5000), "test");
        when(firebaseMessaging.send(any())).thenThrow(new RuntimeException("test", new Throwable()));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.sendToTopic(noteDTO, "test"));

        assertThat(exception).hasMessage("test");
    }

    @Test
    void subscribeToTopic() throws Exception {
        TopicManagementResponse response = mock(TopicManagementResponse.class);
        when(response.getSuccessCount()).thenReturn(3);
        when(firebaseMessaging.subscribeToTopic(anyList(), anyString())).thenReturn(response);

        final Integer result = service.subscribeToTopic(List.of("test"), "test");

        assertThat(result).isEqualTo(3);
    }
}

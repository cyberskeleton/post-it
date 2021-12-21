package com.example.postitserver.controllers;

import com.example.postitserver.models.NoteDTO;
import com.example.postitserver.services.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationController.class)
@ContextConfiguration
class NotificationControllerTest {

    @MockBean
    private NotificationService notificationService;

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        objectMapper = new ObjectMapper()
            .findAndRegisterModules()
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    record Request(List<String> tokens, String topic) {}

    @Test
    void sendNotificationByToken() throws Exception {
        final NoteDTO noteDTO = new NoteDTO("test", "test", null, Instant.ofEpochSecond(5000), "test");
        final String value = objectMapper.writeValueAsString(noteDTO);
        when(notificationService.sendByToken(any())).thenReturn("test");
        final RequestBuilder builder = post("/api/v1/notifications/tokens")
            .accept("application/json")
            .contentType("application/json")
            .content(value);

        mockMvc.perform(builder)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value("Message has been successfully sent to: " + "test"))
            .andDo(print());
    }

    @Test
    void sendNotificationByToken_shouldReturnInternalServerError() throws Exception {
        final NoteDTO noteDTO = new NoteDTO("test", "test", null, Instant.ofEpochSecond(5000), "test");
        final String value = objectMapper.writeValueAsString(noteDTO);
        when(notificationService.sendByToken(any())).thenReturn(null);
        final RequestBuilder builder = post("/api/v1/notifications/tokens")
            .accept("application/json")
            .contentType("application/json")
            .content(value);

        mockMvc.perform(builder)
            .andExpect(status().isInternalServerError())
            .andDo(print());
    }

    @Test
    void sendNotificationToTopic() throws Exception {
        final NoteDTO noteDTO = new NoteDTO("test", "test", null, Instant.ofEpochSecond(5000), "test");
        final String value = objectMapper.writeValueAsString(noteDTO);
        when(notificationService.sendToTopic(any(), anyString())).thenReturn("test");
        final RequestBuilder builder = post("/api/v1/notifications/topics/" + "test")
            .accept("application/json")
            .contentType("application/json")
            .content(value);

        mockMvc.perform(builder)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value("Message has been successfully sent to: " + "test"))
            .andDo(print());
    }

    @Test
    void sendNotificationToTopic_shouldReturnInternalServerError() throws Exception {
        final NoteDTO noteDTO = new NoteDTO("test", "test", null, Instant.ofEpochSecond(5000), "test");
        final String value = objectMapper.writeValueAsString(noteDTO);
        when(notificationService.sendToTopic(any(), anyString())).thenReturn(null);
        final RequestBuilder builder = post("/api/v1/notifications/topics/" + "test")
            .accept("application/json")
            .contentType("application/json")
            .content(value);

        mockMvc.perform(builder)
            .andExpect(status().isInternalServerError())
            .andDo(print());
    }

    @Test
    void subscribeToTopic() throws Exception {
        final Request request = new Request(List.of("test"), "test");
        final String value = objectMapper.writeValueAsString(request);
        when(notificationService.subscribeToTopic(anyList(), anyString())).thenReturn(2);
        final RequestBuilder builder = post("/api/v1/notifications/subscriptions")
            .accept("application/json")
            .contentType("application/json")
            .content(value);

        mockMvc.perform(builder)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value(2 + " tokens have been successfully subscribed to the topic: " + "test"))
            .andDo(print());
    }

    @Test
    void subscribeToTopic_shouldReturnInternalServerError() throws Exception {
        final Request request = new Request(List.of("test"), "test");
        final String value = objectMapper.writeValueAsString(request);
        when(notificationService.subscribeToTopic(anyList(), anyString())).thenReturn(null);
        final RequestBuilder builder = post("/api/v1/notifications/subscriptions")
            .accept("application/json")
            .contentType("application/json")
            .content(value);

        mockMvc.perform(builder)
            .andExpect(status().isInternalServerError())
            .andDo(print());
    }
}

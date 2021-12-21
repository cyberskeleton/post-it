package com.example.postitserver.controllers;

import com.example.postitserver.models.Note;
import com.example.postitserver.models.NoteDTO;
import com.example.postitserver.services.NoteService;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClientController.class)
@ContextConfiguration
class ClientControllerTest {

    @MockBean
    private NoteService noteService;

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

    @Test
    void addNote_shouldReturnOk() throws Exception {
        final NoteDTO noteDTO = new NoteDTO("test", "test", null, Instant.ofEpochSecond(5000), "test");
        when(noteService.addNote(any(NoteDTO.class))).thenReturn(1L);
        final String value = objectMapper.writeValueAsString(noteDTO);
        final RequestBuilder builder = post("/api/v1/notes/add")
            .accept("application/json")
            .contentType("application/json")
            .content(value);

        mockMvc.perform(builder)
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$").value("Notification id: " + 1L))
            .andDo(print());
    }

    @Test
    void addNote_shouldReturnAccepted() throws Exception {
        final NoteDTO noteDTO = new NoteDTO("test", "test", null, Instant.ofEpochSecond(5000), "test");
        when(noteService.addNote(any(NoteDTO.class))).thenReturn(null);
        final String value = objectMapper.writeValueAsString(noteDTO);
        final RequestBuilder builder = post("/api/v1/notes/add")
            .accept("application/json")
            .contentType("application/json")
            .content(value);

        mockMvc.perform(builder)
            .andExpect(status().isAccepted())
            .andDo(print());
    }
}

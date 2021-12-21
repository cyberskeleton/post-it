package com.example.postitserver.controllers;

import com.example.postitserver.models.Note;
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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminController.class)
@ContextConfiguration
class AdminControllerTest {

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

    record Request(String subject) {}

    @Test
    void removeNoteBySubject_shouldReturnNoContent() throws Exception {
        when(noteService.removeNoteBySubject(anyString())).thenReturn(true);
        final String value = objectMapper.writeValueAsString(new Request("test"));
        final RequestBuilder builder = delete("/api/v1/notes/remove")
            .accept("application/json")
            .contentType("application/json")
            .content(value);

        mockMvc.perform(builder)
            .andExpect(status().isNoContent())
            .andDo(print());
    }

    @Test
    void removeNoteBySubject_shouldReturnNotFound() throws Exception {
        when(noteService.removeNoteBySubject(anyString())).thenReturn(false);
        final String value = objectMapper.writeValueAsString(new Request("test"));
        final RequestBuilder builder = delete("/api/v1/notes/remove")
            .accept("application/json")
            .contentType("application/json")
            .content(value);

        mockMvc.perform(builder)
            .andExpect(status().isNotFound())
            .andDo(print());
    }

    @Test
    void getAll_shouldReturnOk() throws Exception {
        final List<Note> notes = List.of(Note.newBuilder().id(1L).subject("test").content("test").token("test").time(Instant.ofEpochSecond(5000)).build());
        when(noteService.getAll()).thenReturn(notes);
        final String value = objectMapper.writeValueAsString(new Request("test"));
        final RequestBuilder builder = get("/api/v1/notes")
            .accept("application/json")
            .contentType("application/json")
            .content(value);

        mockMvc.perform(builder)
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.[0].id").value(1L))
            .andExpect(jsonPath("$.[0].subject").value("test"))
            .andExpect(jsonPath("$.[0].content").value("test"))
            .andExpect(jsonPath("$.[0].token").value("test"))
            .andExpect(jsonPath("$.[0].time").value(Instant.ofEpochSecond(5000).toString()))
            .andDo(print());
    }

    @Test
    void getAll_shouldReturnNotFound() throws Exception {
        final List<Note> notes = List.of();
        when(noteService.getAll()).thenReturn(notes);
        final String value = objectMapper.writeValueAsString(new Request("test"));
        final RequestBuilder builder = get("/api/v1/notes")
            .accept("application/json")
            .contentType("application/json")
            .content(value);

        mockMvc.perform(builder)
            .andExpect(status().isNotFound())
            .andDo(print());
    }
}

package com.example.postitserver.services.impl;

import com.example.postitserver.models.Note;
import com.example.postitserver.models.NoteDTO;
import com.example.postitserver.repositories.NoteRepository;
import com.example.postitserver.services.NoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class NoteServiceImplTest {
    private static final NoteDTO TEST_DTO = new NoteDTO("test", "test", null, Instant.ofEpochSecond(5000), "test");
    private static final Note TEST_ENTITY = Note.newBuilder(TEST_DTO).id(1L).build();

    private NoteRepository noteRepository;
    private NoteService service;

    @BeforeEach
    void setUp() {
        noteRepository = mock(NoteRepository.class);
        service = new NoteServiceImpl(noteRepository);
    }

    @Test
    void addNote() {
        when(noteRepository.save(any(Note.class))).thenReturn(TEST_ENTITY);

        final Long result = service.addNote(TEST_DTO);

        assertThat(result).isEqualTo(1L);
    }

    @Test
    void removeNoteBySubject_shouldReturnTrue() {
        when(noteRepository.findBySubject(anyString())).thenReturn(TEST_ENTITY);
        doNothing().when(noteRepository).delete(any(Note.class));

        final Boolean result = service.removeNoteBySubject("test");

        assertThat(result).isTrue();
        verify(noteRepository, times(1)).delete(any());
    }

    @Test
    void removeNoteBySubject_shouldReturnFalse() {
        when(noteRepository.findBySubject(anyString())).thenReturn(null);

        final Boolean result = service.removeNoteBySubject("test");

        assertThat(result).isFalse();
        verify(noteRepository, never()).delete(any());
    }

    @Test
    void getAll() {
        when(noteRepository.findAll()).thenReturn(List.of(TEST_ENTITY));

        final List<Note> result = service.getAll();

        assertThat(result).containsOnly(TEST_ENTITY);
    }

    @Test
    void findByTimeBetween() {
        when(noteRepository.findByTimeAfterAndTimeBefore(any(), any())).thenReturn(List.of(TEST_ENTITY));

        final List<Note> result = service.findByTimeBetween(Instant.now().minusSeconds(100), Instant.now());

        assertThat(result).containsOnly(TEST_ENTITY);
    }
}

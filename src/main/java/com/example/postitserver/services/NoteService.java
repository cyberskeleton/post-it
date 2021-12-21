package com.example.postitserver.services;

import com.example.postitserver.models.Note;
import com.example.postitserver.models.NoteDTO;

import java.time.Instant;
import java.util.List;

public interface NoteService {

    /**
     * adds note
     * @param noteDTO note Data Transfer Object
     * @return note id
     */
    Long addNote(NoteDTO noteDTO);

    /**
     * removes note by subject
     * @param subject of the note that needs to be removed
     * @return TRUE if successful, else FALSE if note not found
     */
    Boolean removeNoteBySubject(String subject);

    /**
     * gets all notes
     * @return all notes in the repository
     */
    List<Note> getAll();

    /**
     * finds notes in given time frame
     * @param after a certain date and time
     * @param before certain date and time
     * @return all notes between given time points
     */
    List<Note> findByTimeBetween(Instant after, Instant before);
}

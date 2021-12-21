package com.example.postitserver.services.impl;

import com.example.postitserver.models.Note;
import com.example.postitserver.models.NoteDTO;
import com.example.postitserver.repositories.NoteRepository;
import com.example.postitserver.services.NoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class NoteServiceImpl implements NoteService {
    private static final Logger LOG = LoggerFactory.getLogger(NoteServiceImpl.class);

    private final NoteRepository noteRepository;

    public NoteServiceImpl(final NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    /**
     * adds note
     * @param noteDTO note Data Transfer Object
     * @return note id
     */
    @Override
    public Long addNote(final NoteDTO noteDTO) {
        final Note note = this.noteRepository.save(Note.newBuilder(noteDTO).build());
        LOG.info("Note has been successfully saved, id: {}", note.getId());

        return note.getId();
    }

    /**
     * removes note by subject
     * @param subject of the note that needs to be removed
     * @return TRUE if successful, else FALSE if note not found
     */
    @Override
    public Boolean removeNoteBySubject(final String subject) {
        final Note note = this.noteRepository.findBySubject(subject);
        if (note != null) {
            this.noteRepository.delete(note);
            LOG.info("Note has been successfully deleted, id: {}", note.getId());

            return Boolean.TRUE;
        }
        LOG.info("Note has not been found");

        return Boolean.FALSE;
    }

    /**
     * gets all notes
     * @return all notes in the repository
     */
    @Override
    public List<Note> getAll() {
        return this.noteRepository.findAll();
    }

    /**
     * finds notes in given time frame
     * @param after a certain date and time
     * @param before certain date and time
     * @return all notes between given time points
     */
    @Override
    public List<Note> findByTimeBetween(final Instant after, final Instant before) {
        return this.noteRepository.findByTimeAfterAndTimeBefore(after, before);
    }
}

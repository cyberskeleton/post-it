package com.example.postitserver.controllers;

import com.example.postitserver.models.Note;
import com.example.postitserver.services.NoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
@RequestMapping("/api/v1/notes")
public class AdminController {
    private static final Logger LOG = LoggerFactory.getLogger(AdminController.class);

    private final NoteService noteService;

    public AdminController(final NoteService noteService) {
        this.noteService = noteService;
    }

    /**
     * removes note by subject
     * @param subject of the notification
     * @return 204 noContent() if successful, else 404 if note not found
     */
    @DeleteMapping("/remove")
    public ResponseEntity<String> removeNoteBySubject(@RequestBody String subject) {
        LOG.info("Request to remove notification by subject: {}", subject);

        final boolean result = noteService.removeNoteBySubject(subject);

        if (result) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     *  gets all saved notes from the database
     * @return 200 if successful, else return 404
     */

    @GetMapping
    public ResponseEntity<List<Note>> getAll() {
        LOG.info("Request to get all notifications");

        final List<Note> notes = noteService.getAll();

        if (!notes.isEmpty()) {
            return ResponseEntity.ok(notes);
        }
        return ResponseEntity.notFound().build();
    }
}

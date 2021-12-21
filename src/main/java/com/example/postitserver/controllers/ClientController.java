package com.example.postitserver.controllers;

import com.example.postitserver.models.NoteDTO;
import com.example.postitserver.services.NoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notes")
public class ClientController {
    private static final Logger LOG = LoggerFactory.getLogger(ClientController.class);

    private final NoteService noteService;

    public ClientController(final NoteService noteService) {
        this.noteService = noteService;
    }

    /**
     * adds note
     * @param note Data Transfer Object
     * @return 200 if successful, else 202
     */
    @PostMapping("/add")
    public ResponseEntity<String> addNote(@RequestBody NoteDTO note) {
        LOG.info("Request to add a Note: {}", note);

        final Long id = noteService.addNote(note);

        if (id != null) {
            return ResponseEntity.ok("Notification id: " + id);
        }
        return ResponseEntity.accepted().build();
    }
}

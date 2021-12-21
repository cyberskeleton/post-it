package com.example.postitserver.repositories;

import com.example.postitserver.models.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    Note findBySubject(String subject);

    List<Note> findByTimeAfterAndTimeBefore(Instant after, Instant before);
}

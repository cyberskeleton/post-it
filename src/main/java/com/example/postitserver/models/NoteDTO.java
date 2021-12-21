package com.example.postitserver.models;

import java.time.Instant;
import java.util.Map;

public record NoteDTO(String subject, String content, Map<String, String> data, Instant time, String token) {
}

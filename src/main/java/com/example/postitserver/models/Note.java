package com.example.postitserver.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.Instant;
import java.util.Objects;

@Entity
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String subject;
    private String content;
    private Instant time;
    private String token;

    public Note() {
        // empty
    }

    private Note(final Builder builder) {
        id = builder.id;
        subject = builder.subject;
        content = builder.content;
        time = builder.time;
        token = builder.token;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(final NoteDTO copy) {
        Builder builder = new Builder();
        builder.subject = copy.subject();
        builder.content = copy.content();
        builder.time = copy.time();
        builder.token = copy.token();
        return builder;
    }

    public Long getId() {
        return this.id;
    }

    public String getSubject() {
        return this.subject;
    }

    public String getContent() {
        return this.content;
    }

    public Instant getTime() {
        return this.time;
    }

    public String getToken() {
        return this.token;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Note)) return false;
        final Note note = (Note) o;
        return Objects.equals(id, note.id)
            && Objects.equals(subject, note.subject)
            && Objects.equals(content, note.content)
            && Objects.equals(time, note.time)
            && Objects.equals(token, note.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, subject, content, time, token);
    }

    @Override
    public String toString() {
        return "Note{" +
            "id=" + id +
            ", subject='" + subject + '\'' +
            ", content='" + content + '\'' +
            ", time=" + time +
            ", token='" + token + '\'' +
            '}';
    }

    public static final class Builder {
        private Long id;
        private String subject;
        private String content;
        private Instant time;
        private String token;

        private Builder() {
        }

        public Builder id(final Long val) {
            id = val;
            return this;
        }

        public Builder subject(final String val) {
            subject = val;
            return this;
        }

        public Builder content(final String val) {
            content = val;
            return this;
        }

        public Builder time(final Instant val) {
            time = val;
            return this;
        }

        public Builder token(final String val) {
            token = val;
            return this;
        }

        public Note build() {
            return new Note(this);
        }
    }
}

package com.rino.homework06.common.mapping;

import com.google.firebase.Timestamp;
import com.rino.homework06.common.entities.Note;
import com.rino.homework06.common.entities.Priority;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class NoteMapping {

    public static class Fields {
        public static final String TITLE = "title";
        public static final String TEXT = "text";
        public static final String CREATED_AT = "createdAt";
        public static final String PRIORITY = "priority";
    }

    public static Map<String, Object> toDocument(Note note) {
        Map<String, Object> result = new HashMap<>();

        result.put(Fields.TITLE, note.getTitle());
        result.put(Fields.TEXT, note.getText());
        result.put(Fields.CREATED_AT, note.getCreatedAt());
        result.put(Fields.PRIORITY, note.getPriority().name());

        return result;
    }

    public static Note toNote(String id, Map<String, Object> document) {

        String title = (String) document.get(Fields.TITLE);
        String text = (String) document.get(Fields.TEXT);

        Timestamp createdAtTimestamp = (Timestamp) document.get(Fields.CREATED_AT);

        String priorityName = (String) document.get(Fields.PRIORITY);
        Priority priority = Priority.valueOf(priorityName);

        Note note = new Note(
                title,
                text,
                createdAtTimestamp != null ? createdAtTimestamp.toDate() : new Date(),
                priority
        );

        note.setId(id);

        return note;
    }
}

package com.rino.homework06.common.datasources;

import com.rino.homework06.common.entities.Note;

public interface NotesSource {
    Note getNote(int position);

    int getSize();

    void deleteNote(int position);

    void addNote(Note note);

    void updateNote(int position, Note note);

    void deleteAll();
}

package com.rino.homework06.core.datasources;

import com.rino.homework06.core.entities.Note;
import com.rino.homework06.core.utils.Utils;

import java.util.List;

public class NotesSourceImpl implements NotesSource {
    private final List<Note> notes;

    public NotesSourceImpl() {
        notes = Utils.getListOfNotes();
    }

    @Override
    public Note getNote(int position) {
        return notes.get(position);
    }

    @Override
    public int getSize() {
        return notes.size();
    }
}

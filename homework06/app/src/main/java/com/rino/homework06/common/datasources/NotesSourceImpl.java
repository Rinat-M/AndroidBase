package com.rino.homework06.common.datasources;

import com.rino.homework06.common.entities.Note;
import com.rino.homework06.common.handlers.FetchDataCompletedHandler;
import com.rino.homework06.common.utils.Utils;

import java.util.List;

public class NotesSourceImpl implements NotesSource {
    private List<Note> notes;

    public NotesSourceImpl() {
        notes = Utils.getListOfNotes();
    }

    @Override
    public void fetchData(FetchDataCompletedHandler fetchDataCompletedHandler) {
        notes = Utils.getListOfNotes();
        fetchDataCompletedHandler.fetchDataCompleted(this);
    }

    @Override
    public Note getNote(int position) {
        return notes.get(position);
    }

    @Override
    public int getSize() {
        return notes.size();
    }

    @Override
    public void deleteNote(int position) {
        notes.remove(position);
    }

    @Override
    public void addNote(Note note) {
        notes.add(note);
    }

    @Override
    public void updateNote(int position, Note note) {
        notes.set(position, note);
    }

    @Override
    public void deleteAll() {
        notes.clear();
    }
}

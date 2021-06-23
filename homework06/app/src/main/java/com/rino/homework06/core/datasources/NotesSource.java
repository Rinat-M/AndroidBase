package com.rino.homework06.core.datasources;

import com.rino.homework06.core.entities.Note;

public interface NotesSource {
    Note getNote(int position);

    int getSize();
}

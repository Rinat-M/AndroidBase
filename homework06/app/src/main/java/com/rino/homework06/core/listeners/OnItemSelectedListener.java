package com.rino.homework06.core.listeners;

import com.rino.homework06.core.entities.Note;

@FunctionalInterface
public interface OnItemSelectedListener {
    void onItemSelected(Note note);
}

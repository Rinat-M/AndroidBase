package com.rino.homework06.listeners;

import com.rino.homework06.entities.Note;

@FunctionalInterface
public interface OnItemSelectedListener {
    void onItemSelected(Note note);
}

package com.rino.homework06.common.handlers;

import com.rino.homework06.common.entities.Note;

@FunctionalInterface
public interface OnItemSelectedListener {
    void onItemSelected(Note note);
}

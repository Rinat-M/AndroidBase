package com.rino.homework06.common.handlers;

import com.rino.homework06.common.datasources.NotesSource;

@FunctionalInterface
public interface FetchDataCompletedHandler {
    void fetchDataCompleted(NotesSource notesSource);
}

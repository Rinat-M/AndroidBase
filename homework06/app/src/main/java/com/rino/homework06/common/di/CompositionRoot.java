package com.rino.homework06.common.di;

import com.rino.homework06.common.StateStore;
import com.rino.homework06.common.datasources.NotesFirebaseImpl;
import com.rino.homework06.common.datasources.NotesSource;

public class CompositionRoot {

    private NotesSource dataSource;
    private StateStore stateStore;

    public NotesSource getDataSource() {
        if (dataSource == null) {
            dataSource = new NotesFirebaseImpl();
        }

        return dataSource;
    }

    public StateStore getStateStore() {
        if (stateStore == null) {
            stateStore = new StateStore();
        }
        return stateStore;
    }
}

package com.rino.homework06.common.di;

import com.rino.homework06.common.datasources.NotesSource;
import com.rino.homework06.common.datasources.NotesSourceImpl;
import com.rino.homework06.ui.navigation.ScreenNavigator;

public class CompositionRoot {

    private NotesSource dataSource;
    private ScreenNavigator screenNavigator;

    public NotesSource getDataSource() {
        if (dataSource == null) {
            dataSource = new NotesSourceImpl();
        }

        return dataSource;
    }

    public ScreenNavigator getScreenNavigator() {
        if (screenNavigator == null) {
            screenNavigator = new ScreenNavigator();
        }

        return screenNavigator;
    }
}

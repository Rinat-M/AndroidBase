package com.rino.homework06.common.di;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.rino.homework06.common.StateStore;
import com.rino.homework06.common.datasources.NotesSource;
import com.rino.homework06.ui.dialogs.DialogsEventBus;
import com.rino.homework06.ui.navigation.ScreenNavigator;

public class ActivityCompositionRoot {

    private final CompositionRoot compositionRoot;
    private final FragmentActivity activity;

    private ScreenNavigator screenNavigator;
    private StateStore stateStore;

    public ActivityCompositionRoot(CompositionRoot compositionRoot, FragmentActivity activity) {
        this.compositionRoot = compositionRoot;
        this.activity = activity;
    }

    public FragmentActivity getActivity() {
        return activity;
    }

    private FragmentManager getFragmentManager() {
        return getActivity().getSupportFragmentManager();
    }

    public ScreenNavigator getScreenNavigator() {
        if (screenNavigator == null) {
            screenNavigator = new ScreenNavigator(getFragmentManager());
        }
        return screenNavigator;
    }

    public NotesSource getDataSource() {
        return compositionRoot.getDataSource();
    }

    public StateStore getStateStore() {
        return compositionRoot.getStateStore();
    }

    public DialogsEventBus getDialogsEventBus() {
        return compositionRoot.getDialogsEventBus();
    }
}

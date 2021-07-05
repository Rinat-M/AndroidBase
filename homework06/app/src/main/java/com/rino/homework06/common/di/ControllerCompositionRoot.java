package com.rino.homework06.common.di;

import android.content.res.Configuration;

import androidx.fragment.app.FragmentActivity;

import com.rino.homework06.common.StateStore;
import com.rino.homework06.common.datasources.NotesSource;
import com.rino.homework06.ui.navigation.ScreenNavigator;

public class ControllerCompositionRoot {

    private final ActivityCompositionRoot activityCompositionRoot;

    public ControllerCompositionRoot(ActivityCompositionRoot activityCompositionRoot) {
        this.activityCompositionRoot = activityCompositionRoot;
    }

    private FragmentActivity getActivity() {
        return activityCompositionRoot.getActivity();
    }


    public NotesSource getDataSource() {
        return activityCompositionRoot.getDataSource();
    }

    public ScreenNavigator getScreenNavigator() {
        ScreenNavigator screenNavigator = activityCompositionRoot.getScreenNavigator();
        Configuration configuration = getActivity().getResources().getConfiguration();
        screenNavigator.setLandscape(configuration.orientation == Configuration.ORIENTATION_LANDSCAPE);
        return screenNavigator;
    }

    public StateStore getStateStore() {
        return activityCompositionRoot.getStateStore();
    }
}

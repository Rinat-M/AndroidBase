package com.rino.homework06.ui.controllers;

import androidx.appcompat.app.AppCompatActivity;

import com.rino.homework06.common.di.ActivityCompositionRoot;
import com.rino.homework06.common.di.ControllerCompositionRoot;
import com.rino.homework06.ui.CustomApplication;

public class BaseActivity extends AppCompatActivity {

    private ActivityCompositionRoot activityCompositionRoot;
    private ControllerCompositionRoot controllerCompositionRoot;

    public ActivityCompositionRoot getActivityCompositionRoot() {
        if (activityCompositionRoot == null) {
            activityCompositionRoot = new ActivityCompositionRoot(
                    ((CustomApplication) getApplication()).getCompositionRoot(),
                    this);
        }

        return activityCompositionRoot;
    }

    protected ControllerCompositionRoot getCompositionRoot() {
        if (controllerCompositionRoot == null) {
            controllerCompositionRoot = new ControllerCompositionRoot(getActivityCompositionRoot());
        }

        return controllerCompositionRoot;
    }

}

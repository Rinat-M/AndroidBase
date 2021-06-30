package com.rino.homework06.ui;

import android.app.Application;

import com.rino.homework06.common.di.CompositionRoot;

public class CustomApplication extends Application {

    private CompositionRoot compositionRoot;

    @Override
    public void onCreate() {
        super.onCreate();
        compositionRoot = new CompositionRoot();
    }

    public CompositionRoot getCompositionRoot() {
        return compositionRoot;
    }
}

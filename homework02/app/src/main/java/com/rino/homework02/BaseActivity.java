package com.rino.homework02;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class BaseActivity extends AppCompatActivity implements Constants {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        applyTheme();
    }

    protected void changeDayNightMode() {
        saveThemeConfiguration(!isNightModeEnabled());
        applyTheme();
        recreate();
    }

    protected void applyTheme() {
        if (isNightModeEnabled()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    protected boolean isNightModeEnabled() {
        SharedPreferences sharedPref = getSharedPreferences(CALCULATOR_SHARED_PREFERENCES, MODE_PRIVATE);
        return sharedPref.getBoolean(IS_NIGHT_MODE_ENABLED, true);
    }

    protected void saveThemeConfiguration(boolean isNightModeEnabled) {
        SharedPreferences sharedPref = getSharedPreferences(CALCULATOR_SHARED_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(IS_NIGHT_MODE_ENABLED, isNightModeEnabled);
        editor.apply();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}

package com.rino.homework02;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.SwitchCompat;

import com.google.android.material.appbar.MaterialToolbar;

public class SettingsActivity extends BaseActivity {

    public static Intent newInstance(Context packageContext) {
        return new Intent(packageContext, SettingsActivity.class);
    }

    public static void startActivity(Context packageContext) {
        Intent intent = newInstance(packageContext);
        packageContext.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        configureToolbar();

        initViews();
    }

    private void configureToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.material_toolbar);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initViews() {
        SwitchCompat switchCompat = findViewById(R.id.switch_night_mode);
        switchCompat.setChecked(isNightModeEnabled());

        switchCompat.setOnCheckedChangeListener((button, value) -> changeDayNightMode());
    }
}
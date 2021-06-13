package com.rino.homework05;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {
    private static final String CALCULATOR_ACTION = "com.rino.homework02.show_calculator";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {
        MaterialButton buttonRunCalculator = findViewById(R.id.button_run_calculator);
        buttonRunCalculator.setOnClickListener(view -> runCalculator());
    }

    private void runCalculator() {
        Intent calculatorIntent = new Intent(CALCULATOR_ACTION);
        calculatorIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        ActivityInfo activityInfo = calculatorIntent
                .resolveActivityInfo(getPackageManager(), calculatorIntent.getFlags());

        if (activityInfo != null) {
            startActivity(calculatorIntent);
        }
    }
}
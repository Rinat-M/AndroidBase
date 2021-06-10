package com.rino.homework02;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.button.MaterialButton;

import org.jetbrains.annotations.NotNull;


public class MainActivity extends AppCompatActivity {
    private static final String CALCULATOR_KEY = "CALCULATOR_KEY";
    private static final String IS_NIGHT_MODE_ENABLED = "IS_NIGHT_MODE_ENABLED";
    private static final String CALCULATOR_SHARED_PREFERENCES = "CALCULATOR_SHARED_PREFERENCES";

    private Calculator calculator;

    TextView outputLineTextView;
    TextView additionalOutputLineTextView;

    private final View.OnClickListener numberButtonOnClickListener = view -> {
        Button button = (Button) view;
        String buttonText = button.getText().toString();

        String enteredValue = calculator.enteredValue(buttonText);

        outputLineTextView.setText(enteredValue);
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        applyTheme();

        setContentView(R.layout.activity_main);

        calculator = new Calculator();

        initViews();
    }

    private void initViews() {
        initOutputLinesViews();

        initNumberButtonViews();

        initOperationButtonViews();
    }

    private void initOutputLinesViews() {
        outputLineTextView = findViewById(R.id.output_line_text_view);
        outputLineTextView.setText(calculator.getCurrentInput());

        additionalOutputLineTextView = findViewById(R.id.additional_output_line_text_view);
        additionalOutputLineTextView.setText(calculator.getInfoAboutCurrentOperation());
    }

    private void initNumberButtonViews() {
        MaterialButton button0 = findViewById(R.id.button_0);
        MaterialButton button1 = findViewById(R.id.button_1);
        MaterialButton button2 = findViewById(R.id.button_2);
        MaterialButton button3 = findViewById(R.id.button_3);
        MaterialButton button4 = findViewById(R.id.button_4);
        MaterialButton button5 = findViewById(R.id.button_5);
        MaterialButton button6 = findViewById(R.id.button_6);
        MaterialButton button7 = findViewById(R.id.button_7);
        MaterialButton button8 = findViewById(R.id.button_8);
        MaterialButton button9 = findViewById(R.id.button_9);

        MaterialButton buttonComma = findViewById(R.id.button_comma);

        Utils.setOnClickListenerForManyButtons(
                numberButtonOnClickListener,
                buttonComma, button0, button1, button2, button3, button4, button5, button6, button7, button8, button9
        );
    }

    private void initOperationButtonViews() {
        MaterialButton buttonBackspace = findViewById(R.id.button_backspace);
        buttonBackspace.setOnClickListener(view -> outputLineTextView.setText(calculator.removeLastInputValue()));

        Button buttonAC = findViewById(R.id.button_ac);
        buttonAC.setOnClickListener(view -> {
            calculator.reset();
            updateOutputLines();
        });

        MaterialButton buttonPercent = findViewById(R.id.button_percent);
        buttonPercent.setOnClickListener(view -> applyOperationAndUpdateOutputLines(Operation.PERCENT));

        MaterialButton buttonMultiply = findViewById(R.id.button_multiply);
        buttonMultiply.setOnClickListener(view -> applyOperationAndUpdateOutputLines(Operation.MULTIPLY));

        MaterialButton buttonDivide = findViewById(R.id.button_divide);
        buttonDivide.setOnClickListener(view -> applyOperationAndUpdateOutputLines(Operation.DIVIDE));

        MaterialButton buttonMinus = findViewById(R.id.button_minus);
        buttonMinus.setOnClickListener(view -> applyOperationAndUpdateOutputLines(Operation.MINUS));

        MaterialButton buttonPlus = findViewById(R.id.button_plus);
        buttonPlus.setOnClickListener(view -> applyOperationAndUpdateOutputLines(Operation.PLUS));

        MaterialButton buttonCalculate = findViewById(R.id.button_calculate);
        buttonCalculate.setOnClickListener(view -> applyOperationAndUpdateOutputLines(Operation.CALCULATE));

        MaterialButton buttonDayNightMode = findViewById(R.id.button_day_night_mode);
        buttonDayNightMode.setOnClickListener(view -> changeDayNightMode());
    }

    private void applyOperationAndUpdateOutputLines(Operation operation) {
        calculator.applyOperation(operation);
        updateOutputLines();
    }

    private void updateOutputLines() {
        outputLineTextView.setText(calculator.getCurrentInput());
        additionalOutputLineTextView.setText(calculator.getInfoAboutCurrentOperation());
    }


    private void changeDayNightMode() {
        saveThemeConfiguration(!isNightModeEnabled());
        applyTheme();
        recreate();
    }

    private void applyTheme() {
        if (isNightModeEnabled()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    private boolean isNightModeEnabled() {
        SharedPreferences sharedPref = getSharedPreferences(CALCULATOR_SHARED_PREFERENCES, MODE_PRIVATE);
        return sharedPref.getBoolean(IS_NIGHT_MODE_ENABLED, true);
    }

    private void saveThemeConfiguration(boolean isNightModeEnabled) {
        SharedPreferences sharedPref = getSharedPreferences(CALCULATOR_SHARED_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(IS_NIGHT_MODE_ENABLED, isNightModeEnabled);
        editor.apply();
    }

    @Override
    protected void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(CALCULATOR_KEY, calculator);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        calculator = savedInstanceState.getParcelable(CALCULATOR_KEY);
        updateOutputLines();
    }
}

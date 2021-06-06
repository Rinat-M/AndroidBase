package com.rino.homework02;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;


public class MainActivity extends AppCompatActivity {
    private static final String CALCULATOR_KEY = "CALCULATOR_KEY";

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
        Button button0 = findViewById(R.id.button_0);
        Button button1 = findViewById(R.id.button_1);
        Button button2 = findViewById(R.id.button_2);
        Button button3 = findViewById(R.id.button_3);
        Button button4 = findViewById(R.id.button_4);
        Button button5 = findViewById(R.id.button_5);
        Button button6 = findViewById(R.id.button_6);
        Button button7 = findViewById(R.id.button_7);
        Button button8 = findViewById(R.id.button_8);
        Button button9 = findViewById(R.id.button_9);

        Button buttonComma = findViewById(R.id.button_comma);

        Utils.setOnClickListenerForManyButtons(
                numberButtonOnClickListener,
                buttonComma, button0, button1, button2, button3, button4, button5, button6, button7, button8, button9
        );
    }

    private void initOperationButtonViews() {
        ImageButton buttonBackspace = findViewById(R.id.button_backspace);
        buttonBackspace.setOnClickListener(view -> outputLineTextView.setText(calculator.removeLastInputValue()));

        Button buttonAC = findViewById(R.id.button_ac);
        buttonAC.setOnClickListener(view -> {
            calculator.reset();
            updateOutputLines();
        });

        Button buttonPercent = findViewById(R.id.button_percent);
        buttonPercent.setOnClickListener(view -> applyOperationAndUpdateOutputLines(Operation.PERCENT));

        Button buttonMultiply = findViewById(R.id.button_multiply);
        buttonMultiply.setOnClickListener(view -> applyOperationAndUpdateOutputLines(Operation.MULTIPLY));

        Button buttonDivide = findViewById(R.id.button_divide);
        buttonDivide.setOnClickListener(view -> applyOperationAndUpdateOutputLines(Operation.DIVIDE));

        Button buttonMinus = findViewById(R.id.button_minus);
        buttonMinus.setOnClickListener(view -> applyOperationAndUpdateOutputLines(Operation.MINUS));

        Button buttonPlus = findViewById(R.id.button_plus);
        buttonPlus.setOnClickListener(view -> applyOperationAndUpdateOutputLines(Operation.PLUS));

        Button buttonCalculate = findViewById(R.id.button_calculate);
        buttonCalculate.setOnClickListener(view -> applyOperationAndUpdateOutputLines(Operation.CALCULATE));
    }

    private void applyOperationAndUpdateOutputLines(Operation operation) {
        calculator.applyOperation(operation);
        updateOutputLines();
    }

    private void updateOutputLines() {
        outputLineTextView.setText(calculator.getCurrentInput());
        additionalOutputLineTextView.setText(calculator.getInfoAboutCurrentOperation());
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

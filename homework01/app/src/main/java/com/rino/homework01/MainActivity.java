package com.rino.homework01;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // first layout
        // setContentView(R.layout.activity_main);

        // second layout
        // setContentView(R.layout.activity_main_second);

        // third layout with CalendarView
        setContentView(R.layout.activity_main_third);
        CalendarView calendarView = findViewById(R.id.calendar_view);

        if (calendarView != null) {
            TextView selectedDateTextView = findViewById(R.id.selected_date);

            calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) ->
                    selectedDateTextView.setText(String.format("%02d.%02d.%d", dayOfMonth, month, year)));
        }
    }
}
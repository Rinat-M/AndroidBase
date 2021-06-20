package com.rino.homework06.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.rino.homework06.entities.Note;
import com.rino.homework06.utils.Observer;
import com.rino.homework06.entities.Priority;
import com.rino.homework06.R;

import java.util.Calendar;
import java.util.Date;

public class NoteFragment extends Fragment implements Observer {

    public static final String NOTE_FRAGMENT_TAG = "NOTE_FRAGMENT_TAG";

    private static final Calendar CALENDAR = Calendar.getInstance();

    private boolean isViewInitialized = false;
    private Note currentNote;

    private EditText titleEditText;
    private TextView createdAtTextView;
    private CheckBox priorityCheckBox;
    private EditText textEditText;

    public static NoteFragment newInstance() {
        return new NoteFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);

        isViewInitialized = true;

        if (currentNote != null) {
            updateNoteInfo();
        }
    }

    private void initViews(@NonNull View rootView) {
        titleEditText = rootView.findViewById(R.id.note_title_edit_text);
        createdAtTextView = rootView.findViewById(R.id.note_created_at_text_view);
        priorityCheckBox = rootView.findViewById(R.id.note_priority_check_box);
        textEditText = rootView.findViewById(R.id.note_text_edit_text);
    }

    private void updateNoteInfo() {
        titleEditText.setText(currentNote.getTitle());
        textEditText.setText(currentNote.getText());
        priorityCheckBox.setChecked(currentNote.getPriority() == Priority.HIGH);
        createdAtTextView.setText(currentNote.getCreatedAtInFormat());

        initDatePicker(createdAtTextView);
    }

    private void initDatePicker(TextView createdAtTextView) {
        createdAtTextView.setOnClickListener(view -> {
            DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, monthOfYear, dayOfMonth) -> {
                CALENDAR.set(year, monthOfYear, dayOfMonth);
                currentNote.setCreatedAt(new Date(CALENDAR.getTimeInMillis()));
                createdAtTextView.setText(currentNote.getCreatedAtInFormat());
            };

            CALENDAR.setTime(currentNote.getCreatedAt());

            DatePickerDialog dialog = new DatePickerDialog(
                    requireContext(),
                    dateSetListener,
                    CALENDAR.get(Calendar.YEAR),
                    CALENDAR.get(Calendar.MONTH),
                    CALENDAR.get(Calendar.DAY_OF_MONTH)
            );

            dialog.show();
        });
    }

    @Override
    public void updateNote(Note note) {
        currentNote = note;

        if (currentNote != null && isViewInitialized) {
            updateNoteInfo();
        }
    }
}
package com.rino.homework06.ui.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.rino.homework06.R;
import com.rino.homework06.common.datasources.NotesSource;
import com.rino.homework06.common.entities.Note;
import com.rino.homework06.common.entities.Priority;
import com.rino.homework06.ui.navigation.ScreenNavigator;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NoteFragment extends BaseFragment {

    public static final String NOTE_FRAGMENT_TAG = "NOTE_FRAGMENT_TAG";
    public static final String POSITION = "POSITION";

    private static final Calendar CALENDAR = Calendar.getInstance();

    private Note currentNote;

    private NotesSource dataSource;
    private ScreenNavigator screenNavigator;

    private EditText titleEditText;
    private TextView createdAtTextView;
    private CheckBox priorityCheckBox;
    private EditText textEditText;

    private Date datePickerDate;

    private int currentPosition;

    private boolean isDeleted = false;

    public static NoteFragment newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt(POSITION, position);
        NoteFragment fragment = new NoteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        if (getArguments() != null) {
            currentPosition = getArguments().getInt(POSITION);
        }

        screenNavigator = getCompositionRoot().getScreenNavigator();
        dataSource = getCompositionRoot().getDataSource();

        currentNote = dataSource.getNote(currentPosition);
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

        if (currentNote != null) {
            updateNoteInfo();
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_fragment, menu);

        MenuItem actionSort = menu.findItem(R.id.action_sort);
        actionSort.setVisible(false);

        MenuItem actionSearch = menu.findItem(R.id.action_search);
        actionSearch.setVisible(false);

        if (!screenNavigator.isLandscape()) {
            MenuItem actionAdd = menu.findItem(R.id.action_add);
            actionAdd.setVisible(false);
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

        datePickerDate = currentNote.getCreatedAt();
        createdAtTextView.setText(currentNote.getCreatedAtInFormat());

        initDatePicker();
    }

    private void initDatePicker() {
        createdAtTextView.setOnClickListener(view -> {
            DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, monthOfYear, dayOfMonth) -> {
                CALENDAR.set(year, monthOfYear, dayOfMonth);

                datePickerDate = new Date(CALENDAR.getTimeInMillis());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
                createdAtTextView.setText(simpleDateFormat.format(datePickerDate));
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
    public void onStop() {
        super.onStop();

        if (!isDeleted) {
            saveData();
        }
    }

    private void saveData() {
        String title = titleEditText == null ? "" : titleEditText.getText().toString();
        String text = textEditText == null ? "" : textEditText.getText().toString();
        Priority priority = priorityCheckBox.isChecked() ? Priority.HIGH : Priority.NORMAL;

        Note updatedNote = new Note(title, text, datePickerDate, priority);
        updatedNote.setId(currentNote.getId());

        dataSource.updateNote(currentPosition, updatedNote);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_delete) {
            dataSource.deleteNote(currentPosition);
            isDeleted = true;

            screenNavigator.toListOfNotesScreen();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
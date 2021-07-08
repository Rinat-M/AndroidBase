package com.rino.homework06.ui.dialogs.notedialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.rino.homework06.R;
import com.rino.homework06.common.datasources.NotesSource;
import com.rino.homework06.common.entities.Note;
import com.rino.homework06.common.entities.Priority;
import com.rino.homework06.common.listeners.OnActionDialogListener;
import com.rino.homework06.ui.dialogs.BaseBottomSheetDialogFragment;
import com.rino.homework06.ui.dialogs.DialogsEventBus;

import java.util.Calendar;
import java.util.Date;

public class NoteBottomSheetDialogFragment extends BaseBottomSheetDialogFragment
        implements OnActionDialogListener {

    private static final String POSITION = "POSITION";
    private static final String ARG_POSITIVE_BUTTON_CAPTION = "ARG_POSITIVE_BUTTON_CAPTION";
    private static final String ARG_NEGATIVE_BUTTON_CAPTION = "ARG_NEGATIVE_BUTTON_CAPTION";

    private static final String UPDATED_NOTE = "UPDATED_NOTE";

    private static final Calendar CALENDAR = Calendar.getInstance();

    private Note originalNote;
    private Note updatedNote;

    private NotesSource dataSource;
    private DialogsEventBus dialogsEventBus;

    private EditText titleEditText;
    private TextView createdAtTextView;
    private CheckBox priorityCheckBox;
    private EditText textEditText;
    private MaterialButton positiveButton;
    private MaterialButton negativeButton;

    private String positiveButtonCaption;
    private String negativeButtonCaption;

    private Integer currentPosition;

    private boolean isNew;

    public static NoteBottomSheetDialogFragment newInstance(
            Integer position,
            String positiveButtonCaption,
            String negativeButtonCaption
    ) {
        Bundle args = new Bundle(3);

        NoteBottomSheetDialogFragment fragment = new NoteBottomSheetDialogFragment();

        args.putSerializable(POSITION, position);
        args.putString(ARG_POSITIVE_BUTTON_CAPTION, positiveButtonCaption);
        args.putString(ARG_NEGATIVE_BUTTON_CAPTION, negativeButtonCaption);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dialogsEventBus = getCompositionRoot().getDialogsEventBus();

        if (getArguments() == null) {
            throw new IllegalArgumentException("arguments mustn't be null!");
        }

        currentPosition = (Integer) getArguments().getSerializable(POSITION);
        positiveButtonCaption = getArguments().getString(ARG_POSITIVE_BUTTON_CAPTION);
        negativeButtonCaption = getArguments().getString(ARG_NEGATIVE_BUTTON_CAPTION);

        isNew = currentPosition == null;

        dialogsEventBus = getCompositionRoot().getDialogsEventBus();

        dataSource = getCompositionRoot().getDataSource();

        if (!isNew) {
            originalNote = dataSource.getNote(currentPosition);
        }

        if (savedInstanceState == null) {
            if (isNew) {
                updatedNote = new Note("Тема заметки", "Текст заметки", new Date(), Priority.NORMAL);
            } else {
                updatedNote = originalNote.getCopy();
            }
        } else {
            updatedNote = savedInstanceState.getParcelable(UPDATED_NOTE);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new BottomSheetDialog(requireContext());
        dialog.setContentView(R.layout.fragment_note_dialog);

        initViews(dialog);

        updateNoteInfo();

        return dialog;
    }

    private void initViews(@NonNull Dialog dialog) {
        titleEditText = dialog.findViewById(R.id.note_title_edit_text);
        createdAtTextView = dialog.findViewById(R.id.note_created_at_text_view);
        priorityCheckBox = dialog.findViewById(R.id.note_priority_check_box);
        textEditText = dialog.findViewById(R.id.note_text_edit_text);
        positiveButton = dialog.findViewById(R.id.positive_button);
        negativeButton = dialog.findViewById(R.id.negative_button);

        positiveButton.setOnClickListener(view -> onPositiveButtonClicked());
        negativeButton.setOnClickListener(view -> onNegativeButtonClicked());
    }

    private void updateNoteInfo() {
        titleEditText.setText(updatedNote.getTitle());
        textEditText.setText(updatedNote.getText());
        priorityCheckBox.setChecked(updatedNote.getPriority() == Priority.HIGH);

        positiveButton.setText(positiveButtonCaption);
        negativeButton.setText(negativeButtonCaption);

        createdAtTextView.setText(updatedNote.getCreatedAtInFormat());

        initDatePicker();
    }

    private void initDatePicker() {
        createdAtTextView.setOnClickListener(view -> {
            DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, monthOfYear, dayOfMonth) -> {
                CALENDAR.set(year, monthOfYear, dayOfMonth);

                updatedNote.setCreatedAt(new Date(CALENDAR.getTimeInMillis()));
                createdAtTextView.setText(updatedNote.getCreatedAtInFormat());
            };

            CALENDAR.setTime(updatedNote.getCreatedAt());

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
        saveUpdatedData();
    }

    private void saveUpdatedData() {
        String title = titleEditText == null ? "" : titleEditText.getText().toString();
        String text = textEditText == null ? "" : textEditText.getText().toString();
        Priority priority = priorityCheckBox.isChecked() ? Priority.HIGH : Priority.NORMAL;

        updatedNote.setTitle(title);
        updatedNote.setText(text);
        updatedNote.setPriority(priority);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(UPDATED_NOTE, updatedNote);
    }

    @Override
    public void onPositiveButtonClicked() {
        saveUpdatedData();

        if (isNew) {
            dataSource.addNote(updatedNote);
        } else {
            dataSource.updateNote(currentPosition, updatedNote);
        }

        dialogsEventBus.postEvent(new NoteDialogEvent(NoteDialogEvent.Button.POSITIVE));
        dismiss();
    }

    @Override
    public void onNegativeButtonClicked() {
        dialogsEventBus.postEvent(new NoteDialogEvent(NoteDialogEvent.Button.NEGATIVE));
        dismiss();
    }
}
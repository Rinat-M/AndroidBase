package com.rino.homework06.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.rino.homework06.entities.Note;
import com.rino.homework06.listeners.OnItemSelectedListener;
import com.rino.homework06.entities.Priority;
import com.rino.homework06.R;
import com.rino.homework06.utils.Utils;

import java.util.List;

public class ListOfNotesFragment extends Fragment {
    public static final String LIST_OF_NOTES_FRAGMENT_TAG = "LIST_OF_NOTES_FRAGMENT_TAG";

    private OnItemSelectedListener onItemSelectedListener;

    public static ListOfNotesFragment newInstance() {
        return new ListOfNotesFragment();
    }

    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_of_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addListOfNotes(view);
    }

    private void addListOfNotes(View view) {
        LinearLayout linearLayout = view.findViewById(R.id.list_of_notes_container);

        List<Note> notes = Utils.getListOfNotes();

        for (Note note : notes) {
            View itemView = getLayoutInflater().inflate(R.layout.list_of_notes_item, linearLayout, false);

            fillNoteItemView(itemView, note);

            linearLayout.addView(itemView);
        }
    }

    private void fillNoteItemView(View itemView, Note note) {
        TextView titleTextView = itemView.findViewById(R.id.title);
        titleTextView.setText(note.getTitle());

        if (note.getPriority() == Priority.HIGH) {
            View priorityView = itemView.findViewById(R.id.priority);
            priorityView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red_500));
        }

        TextView createdAtTextView = itemView.findViewById(R.id.created_at);
        createdAtTextView.setText(note.getCreatedAtInFormat());

        ConstraintLayout constraint = itemView.findViewById(R.id.note_item_container);
        constraint.setOnClickListener(view -> onItemSelectedListener.onItemSelected(note));
    }
}
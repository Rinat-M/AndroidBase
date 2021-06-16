package com.rino.homework06;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.List;

public class ListOfNotesFragment extends Fragment implements Observer {
    public static final String LIST_OF_NOTES_FRAGMENT_TAG = "LIST_OF_NOTES_FRAGMENT_TAG";
    private static final String SELECTED_NOTE = "SELECTED_NOTE";

    private boolean isLandscape;

    private OnItemSelectedListener onItemSelectedListener;

    private Note selectedNote = null;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        Configuration configuration = getResources().getConfiguration();
        isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    public static ListOfNotesFragment newInstance(Note note) {
        ListOfNotesFragment fragment = new ListOfNotesFragment();
        Bundle args = new Bundle();
        args.putParcelable(SELECTED_NOTE, note);
        fragment.setArguments(args);
        return fragment;
    }

    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            selectedNote = getArguments().getParcelable(SELECTED_NOTE);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SELECTED_NOTE, selectedNote);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null) {
            selectedNote = savedInstanceState.getParcelable(SELECTED_NOTE);
        }
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

        if (isLandscape && selectedNote != null) {
            NoteFragment noteFragment = NoteFragment.newInstance(selectedNote);

            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.note_container, noteFragment, NoteFragment.NOTE_FRAGMENT_TAG)
                    .commit();
        } else if (selectedNote != null) {
            NoteFragment noteFragment = NoteFragment.newInstance(selectedNote);

            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.list_of_notes, noteFragment, NoteFragment.NOTE_FRAGMENT_TAG)
                    .commit();
        }
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
        constraint.setOnClickListener(view -> {
            onItemSelectedListener.onItemSelected(note);

            selectedNote = note;

            NoteFragment noteFragment = NoteFragment.newInstance(selectedNote);

            int containerViewId = R.id.note_container;

            if (!isLandscape) {
                containerViewId = R.id.list_of_notes;
            }

            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .replace(containerViewId, noteFragment, NoteFragment.NOTE_FRAGMENT_TAG)
                    .commit();
        });
    }

    @Override
    public void updateNote(Note note) {
        selectedNote = note;

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

        if (selectedNote != null) {
            NoteFragment noteFragment = NoteFragment.newInstance(selectedNote);

            int containerViewId = R.id.note_container;

            if (!isLandscape) {
                containerViewId = R.id.list_of_notes;
            }

            fragmentManager
                    .beginTransaction()
                    .replace(containerViewId, noteFragment, NoteFragment.NOTE_FRAGMENT_TAG)
                    .commit();
        } else {
            Fragment fragment = fragmentManager.findFragmentByTag(NoteFragment.NOTE_FRAGMENT_TAG);

            if (fragment != null) {
                fragmentManager
                        .beginTransaction()
                        .remove(fragment)
                        .commit();
            }
        }
    }
}
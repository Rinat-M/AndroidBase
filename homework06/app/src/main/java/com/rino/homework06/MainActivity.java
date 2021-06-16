package com.rino.homework06;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity {
    private static final String SELECTED_NOTE = "SELECTED_NOTE";

    private Note selectedNote;

    private final Publisher publisher = new Publisher();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();

        ListOfNotesFragment listOfNotesFragment = ListOfNotesFragment.newInstance(selectedNote);
        listOfNotesFragment.setOnItemSelectedListener(note -> selectedNote = note);

        publisher.subscribe(listOfNotesFragment);

        fragmentManager
                .beginTransaction()
                .replace(R.id.list_of_notes, listOfNotesFragment, ListOfNotesFragment.LIST_OF_NOTES_FRAGMENT_TAG)
                .commit();
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        selectedNote = savedInstanceState.getParcelable(SELECTED_NOTE);
        publisher.notify(selectedNote);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SELECTED_NOTE, selectedNote);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment fragment = fragmentManager.findFragmentByTag(ListOfNotesFragment.LIST_OF_NOTES_FRAGMENT_TAG);

        if (fragment == null) {
            selectedNote = null;

            ListOfNotesFragment listOfNotesFragment = ListOfNotesFragment.newInstance(null);
            listOfNotesFragment.setOnItemSelectedListener(note -> selectedNote = note);

            publisher.subscribe(listOfNotesFragment);

            fragmentManager
                    .beginTransaction()
                    .replace(R.id.list_of_notes, listOfNotesFragment, ListOfNotesFragment.LIST_OF_NOTES_FRAGMENT_TAG)
                    .commit();

        } else {
            super.onBackPressed();
        }
    }
}
package com.rino.homework06;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.rino.homework06.entities.Note;
import com.rino.homework06.fragments.FragmentEnum;
import com.rino.homework06.fragments.ListOfNotesFragment;
import com.rino.homework06.fragments.NoteFragment;
import com.rino.homework06.utils.Publisher;
import com.rino.homework06.utils.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final String SELECTED_NOTE = "SELECTED_NOTE";

    private static final Map<FragmentEnum, Fragment> fragmentMap;

    private static final Map<FragmentEnum, String> fragmentTagMap;

    static {
        fragmentMap = new HashMap<>();
        fragmentMap.put(FragmentEnum.LIST_OF_NOTES, ListOfNotesFragment.newInstance());
        fragmentMap.put(FragmentEnum.NOTE, NoteFragment.newInstance());

        fragmentTagMap = new HashMap<>();
        fragmentTagMap.put(FragmentEnum.LIST_OF_NOTES, ListOfNotesFragment.LIST_OF_NOTES_FRAGMENT_TAG);
        fragmentTagMap.put(FragmentEnum.NOTE, NoteFragment.NOTE_FRAGMENT_TAG);
    }


    private FragmentEnum currentFragmentEntry = FragmentEnum.LIST_OF_NOTES;

    private Note selectedNote;

    private final Publisher publisher = new Publisher();

    private boolean isLandscape;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();

        Configuration configuration = getResources().getConfiguration();
        isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE;

        configureFragmentMap();

        configureFragments();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void configureFragmentMap() {
        ListOfNotesFragment listOfNotesFragment = (ListOfNotesFragment) fragmentMap.get(FragmentEnum.LIST_OF_NOTES);
        if (listOfNotesFragment != null) {
            listOfNotesFragment.setOnItemSelectedListener(note -> {
                selectedNote = note;
                notifyAndConfigureFragments();
            });
        }

        NoteFragment noteFragment = (NoteFragment) fragmentMap.get(FragmentEnum.NOTE);
        publisher.subscribe(noteFragment);
    }

    private void notifyAndConfigureFragments() {
        publisher.notify(selectedNote);
        configureFragments();
    }

    private void configureFragments() {
        if (isLandscape && selectedNote != null) {
            replaceFragment(FragmentEnum.NOTE, R.id.note_container);
        } else if (selectedNote != null) {
            replaceFragment(FragmentEnum.NOTE);
        } else {
            removeFragment(FragmentEnum.NOTE);
            replaceFragment(FragmentEnum.LIST_OF_NOTES);
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        selectedNote = savedInstanceState.getParcelable(SELECTED_NOTE);
        notifyAndConfigureFragments();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SELECTED_NOTE, selectedNote);
    }

    private void replaceFragment(FragmentEnum fragmentType) {
        int containerId = R.id.list_of_notes;

        replaceFragment(fragmentType, containerId);
    }

    private void replaceFragment(FragmentEnum fragmentType, @IdRes int containerId) {
        currentFragmentEntry = fragmentType;

        FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment fragment = Objects.requireNonNull(fragmentMap.get(fragmentType));
        String fragmentTag = fragmentTagMap.get(fragmentType);

        fragmentManager
                .beginTransaction()
                .replace(containerId, fragment, fragmentTag)
                .commit();
    }

    private void removeFragment(FragmentEnum fragmentType) {
        String fragmentTag = fragmentTagMap.get(fragmentType);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(fragmentTag);

        if (fragment != null) {
            fragmentManager
                    .beginTransaction()
                    .remove(fragment)
                    .commit();
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId) {
            case R.id.action_add:
            case R.id.action_sort:
                Utils.showToastShort(MainActivity.this, getString(R.string.not_implemented));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem search = menu.findItem(R.id.action_search);
        SearchView searchText = (SearchView) search.getActionView();

        searchText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Utils.showToastShort(MainActivity.this, getString(R.string.not_implemented));
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        return true;
    }

    @Override
    public void onBackPressed() {
        if (!isLandscape && currentFragmentEntry == FragmentEnum.NOTE) {
            selectedNote = null;
            notifyAndConfigureFragments();
        } else {
            super.onBackPressed();
        }
    }
}
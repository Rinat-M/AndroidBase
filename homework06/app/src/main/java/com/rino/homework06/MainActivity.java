package com.rino.homework06;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;
import com.rino.homework06.entities.Note;
import com.rino.homework06.fragments.AboutFragment;
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
    private static final String CURRENT_FRAGMENT = "CURRENT_FRAGMENT";

    private static final Map<FragmentEnum, Fragment> fragmentMap;

    private static final Map<FragmentEnum, String> fragmentTagMap;

    static {
        fragmentMap = new HashMap<>();
        fragmentMap.put(FragmentEnum.LIST_OF_NOTES, ListOfNotesFragment.newInstance());
        fragmentMap.put(FragmentEnum.NOTE, NoteFragment.newInstance());
        fragmentMap.put(FragmentEnum.ABOUT, AboutFragment.newInstance());

        fragmentTagMap = new HashMap<>();
        fragmentTagMap.put(FragmentEnum.LIST_OF_NOTES, ListOfNotesFragment.LIST_OF_NOTES_FRAGMENT_TAG);
        fragmentTagMap.put(FragmentEnum.NOTE, NoteFragment.NOTE_FRAGMENT_TAG);
        fragmentTagMap.put(FragmentEnum.ABOUT, AboutFragment.ABOUT_FRAGMENT_TAG);
    }


    private FragmentEnum currentFragmentEntry = FragmentEnum.LIST_OF_NOTES;

    private Note selectedNote;

    private final Publisher publisher = new Publisher();

    private boolean isLandscape;

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        Configuration configuration = getResources().getConfiguration();
        isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE;

        configureFragmentMap();

        configureFragments(currentFragmentEntry);
    }

    private void initView() {
        Toolbar toolbar = initAndGetToolbar();
        initDrawer(toolbar);
    }

    private Toolbar initAndGetToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        return toolbar;
    }

    private void initDrawer(Toolbar toolbar) {
        drawerLayout = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        configureNavigationView();
    }

    private void configureNavigationView() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                item -> {
                    int itemId = item.getItemId();

                    if (navigateFragment(itemId)) {
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    }

                    return false;
                }
        );
    }

    private void configureFragmentMap() {
        ListOfNotesFragment listOfNotesFragment = (ListOfNotesFragment) fragmentMap.get(FragmentEnum.LIST_OF_NOTES);
        if (listOfNotesFragment != null) {
            listOfNotesFragment.setOnItemSelectedListener(note -> {
                selectedNote = note;
                notifyAndConfigureFragments(FragmentEnum.NOTE);
            });
        }

        NoteFragment noteFragment = (NoteFragment) fragmentMap.get(FragmentEnum.NOTE);
        publisher.subscribe(noteFragment);
    }

    private void notifyAndConfigureFragments(FragmentEnum fragmentType) {
        publisher.notify(selectedNote);
        configureFragments(fragmentType);
    }

    private void configureFragments(FragmentEnum fragmentType) {
        switch (fragmentType) {
            case NOTE:
            case LIST_OF_NOTES:
                if (isLandscape && selectedNote != null) {
                    replaceFragment(FragmentEnum.NOTE, R.id.note_container);
                } else if (selectedNote != null) {
                    replaceFragment(FragmentEnum.NOTE);
                } else {
                    removeFragment(FragmentEnum.NOTE);
                    replaceFragment(FragmentEnum.LIST_OF_NOTES);
                }
                break;
            case ABOUT:
                removeFragment(FragmentEnum.NOTE);

                if (isLandscape) {
                    replaceFragment(FragmentEnum.ABOUT, R.id.note_container);
                } else {
                    replaceFragment(FragmentEnum.ABOUT);
                }

                break;
        }


//        if (isLandscape && selectedNote != null) {
//            replaceFragment(FragmentEnum.NOTE, R.id.note_container);
//        } else if (selectedNote != null) {
//            replaceFragment(FragmentEnum.NOTE);
//        } else {
//            removeFragment(FragmentEnum.NOTE);
//
//            if (currentFragmentEntry == FragmentEnum.ABOUT) {
//                replaceFragment(FragmentEnum.ABOUT);
//            } else {
//                replaceFragment(FragmentEnum.LIST_OF_NOTES);
//            }
//        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        selectedNote = savedInstanceState.getParcelable(SELECTED_NOTE);

        String currentFragment = savedInstanceState.getString(CURRENT_FRAGMENT);
        currentFragmentEntry = FragmentEnum.valueOf(currentFragment);

        notifyAndConfigureFragments(currentFragmentEntry);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SELECTED_NOTE, selectedNote);
        outState.putString(CURRENT_FRAGMENT, currentFragmentEntry.name());
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (navigateFragment(itemId)) {
            return true;
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

    @SuppressLint("NonConstantResourceId")
    private boolean navigateFragment(int itemId) {
        switch (itemId) {
            case R.id.action_add:
            case R.id.action_sort:
            case R.id.action_settings:
                Utils.showToastShort(MainActivity.this, getString(R.string.not_implemented));
                return true;

            case R.id.action_notes:
                selectedNote = null;
                notifyAndConfigureFragments(FragmentEnum.LIST_OF_NOTES);
                return true;

            case R.id.action_about:
                configureFragments(FragmentEnum.ABOUT);
                return true;
        }

        return false;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (!isLandscape && currentFragmentEntry == FragmentEnum.NOTE) {
            selectedNote = null;
            notifyAndConfigureFragments(FragmentEnum.LIST_OF_NOTES);
        } else {
            super.onBackPressed();
        }
    }
}
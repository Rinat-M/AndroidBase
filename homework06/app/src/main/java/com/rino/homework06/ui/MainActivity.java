package com.rino.homework06.ui;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.rino.homework06.R;
import com.rino.homework06.common.di.CompositionRoot;
import com.rino.homework06.common.utils.Utils;
import com.rino.homework06.ui.fragments.FragmentEnum;
import com.rino.homework06.ui.navigation.ScreenNavigator;

public class MainActivity extends AppCompatActivity {
    private ScreenNavigator screenNavigator;

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        CompositionRoot compositionRoot = ((CustomApplication) getApplication()).getCompositionRoot();

        screenNavigator = compositionRoot.getScreenNavigator();
        screenNavigator.setFragmentManager(this.getSupportFragmentManager());

        Configuration configuration = getResources().getConfiguration();
        screenNavigator.setLandscape(configuration.orientation == Configuration.ORIENTATION_LANDSCAPE);

        screenNavigator.toListOfNotesScreen(true);
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

                    if (processMenuSelection(itemId)) {
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    }

                    return false;
                }
        );
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (processMenuSelection(itemId)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("NonConstantResourceId")
    private boolean processMenuSelection(int itemId) {
        switch (itemId) {
            case R.id.action_sort:
            case R.id.action_settings:
                Utils.showToastShort(MainActivity.this, getString(R.string.not_implemented));
                return true;

            case R.id.action_notes:
                screenNavigator.toListOfNotesScreen();
                screenNavigator.resetSelectedPosition();
                return true;

            case R.id.action_about:
                screenNavigator.toAboutScreen();
                return true;
        }

        return false;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (!screenNavigator.isLandscape() && screenNavigator.getCurrentFragmentEntry() == FragmentEnum.NOTE) {
            screenNavigator.toListOfNotesScreen();
            screenNavigator.resetSelectedPosition();
        } else {
            super.onBackPressed();
        }
    }
}
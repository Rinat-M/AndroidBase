package com.rino.homework06.ui.navigation;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.rino.homework06.R;
import com.rino.homework06.ui.fragments.AboutFragment;
import com.rino.homework06.ui.fragments.FragmentEnum;
import com.rino.homework06.ui.fragments.ListOfNotesFragment;
import com.rino.homework06.ui.fragments.NoteFragment;

public class ScreenNavigator {

    private FragmentManager fragmentManager;

    private FragmentEnum currentFragmentEntry = FragmentEnum.LIST_OF_NOTES;

    private boolean isLandscape;

    private int selectedPosition = -1;

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public void addFragment(Fragment fragment, String fragmentTag) {
        addFragment(fragment, fragmentTag, R.id.list_of_notes);
    }

    public void addFragment(Fragment fragment, String fragmentTag, @IdRes int containerId) {
        fragmentManager
                .beginTransaction()
                .replace(containerId, fragment, fragmentTag)
                .commit();
    }

    public void removeFragment(String fragmentTag) {
        Fragment fragment = fragmentManager.findFragmentByTag(fragmentTag);

        if (fragment != null) {
            fragmentManager
                    .beginTransaction()
                    .remove(fragment)
                    .commit();
        }
    }

    public FragmentEnum getCurrentFragmentEntry() {
        return currentFragmentEntry;
    }

    public void setLandscape(boolean landscape) {
        isLandscape = landscape;
    }

    public boolean isLandscape() {
        return isLandscape;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void resetSelectedPosition() {
        selectedPosition = -1;
    }

    public void toListOfNotesScreen() {
        toListOfNotesScreen(false);
    }

    public void toListOfNotesScreen(boolean restoreFragmentEntry) {
        removeFragment(NoteFragment.NOTE_FRAGMENT_TAG);

        if (!restoreFragmentEntry) {
            currentFragmentEntry = FragmentEnum.LIST_OF_NOTES;
        }

        Fragment fragment = ListOfNotesFragment.newInstance();
        addFragment(fragment, ListOfNotesFragment.LIST_OF_NOTES_FRAGMENT_TAG);
    }

    public void toNoteScreen() {
        currentFragmentEntry = FragmentEnum.NOTE;
        Fragment fragment = NoteFragment.newInstance(selectedPosition);
        addFragment(fragment, NoteFragment.NOTE_FRAGMENT_TAG, getContainerForAdditionalScreen());
    }

    public void toAboutScreen() {
        currentFragmentEntry = FragmentEnum.ABOUT;
        Fragment fragment = AboutFragment.newInstance();
        addFragment(fragment, AboutFragment.ABOUT_FRAGMENT_TAG, getContainerForAdditionalScreen());
    }

    private int getContainerForAdditionalScreen() {
        return isLandscape ? R.id.note_container : R.id.list_of_notes;
    }
}

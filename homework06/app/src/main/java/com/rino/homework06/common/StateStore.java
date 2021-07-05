package com.rino.homework06.common;

import com.rino.homework06.ui.fragments.FragmentEnum;

public class StateStore {

    private int selectedPosition = -1;
    private FragmentEnum lastFragmentEntry;

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public void resetSelectedPosition() {
        selectedPosition = -1;
    }

    public FragmentEnum getLastFragmentEntry() {
        return lastFragmentEntry;
    }

    public void setLastFragmentEntry(FragmentEnum lastFragmentEntry) {
        this.lastFragmentEntry = lastFragmentEntry;
    }
}

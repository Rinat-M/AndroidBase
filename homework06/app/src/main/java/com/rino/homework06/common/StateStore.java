package com.rino.homework06.common;

import com.rino.homework06.ui.fragments.FragmentEnum;

public class StateStore {

    private int selectedPosition = -1;
    private FragmentEnum lastFragmentEntry;
    private int positionToDelete = -1;

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

    public int getPositionToDelete() {
        return positionToDelete;
    }

    public void setPositionToDelete(int positionToDelete) {
        this.positionToDelete = positionToDelete;
    }

    public void resetPositionToDelete() {
        positionToDelete = -1;
    }
}

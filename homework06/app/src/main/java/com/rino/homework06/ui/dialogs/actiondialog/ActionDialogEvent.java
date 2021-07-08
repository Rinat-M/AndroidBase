package com.rino.homework06.ui.dialogs.actiondialog;

public class ActionDialogEvent {

    public enum Button {
        POSITIVE, NEGATIVE
    }

    private final Button clickedButton;

    public ActionDialogEvent(Button clickedButton) {
        this.clickedButton = clickedButton;
    }

    public Button getClickedButton() {
        return clickedButton;
    }
}

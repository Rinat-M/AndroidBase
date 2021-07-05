package com.rino.homework06.ui.dialogs;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.rino.homework06.R;
import com.rino.homework06.common.listeners.OnActionDialogListener;
import com.rino.homework06.ui.dialogs.actiondialog.ActionBottomSheetDialogFragment;

public class DialogsManager {

    private Context context;
    private FragmentManager fragmentManager;

    public DialogsManager(Context context, FragmentManager fragmentManager) {
        this.context = context;
        this.fragmentManager = fragmentManager;
    }


    public void showDeleteActionDialog(@Nullable String tag) {
        BottomSheetDialogFragment dialogFragment = ActionBottomSheetDialogFragment.newInstance(
                getString(R.string.delete_note_title),
                getString(R.string.delete_note_message),
                getString(R.string.positive_button_caption),
                getString(R.string.negative_button_caption)
        );

        dialogFragment.show(fragmentManager, tag);
    }

    public String getString(int stringId) {
        return context.getString(stringId);
    }
}

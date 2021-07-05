package com.rino.homework06.ui.dialogs.actiondialog;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.rino.homework06.R;
import com.rino.homework06.common.listeners.OnActionDialogListener;
import com.rino.homework06.ui.dialogs.BaseBottomSheetDialogFragment;
import com.rino.homework06.ui.dialogs.DialogsEventBus;

public class ActionBottomSheetDialogFragment extends BaseBottomSheetDialogFragment
        implements OnActionDialogListener {

    protected static final String ARG_TITLE = "ARG_TITLE";
    protected static final String ARG_MESSAGE = "ARG_MESSAGE";
    protected static final String ARG_POSITIVE_BUTTON_CAPTION = "ARG_POSITIVE_BUTTON_CAPTION";
    protected static final String ARG_NEGATIVE_BUTTON_CAPTION = "ARG_NEGATIVE_BUTTON_CAPTION";

    private String title;
    private String message;
    private String positiveButtonCaption;
    private String negativeButtonCaption;

    private DialogsEventBus dialogsEventBus;

    public static ActionBottomSheetDialogFragment newInstance(String title, String message, String positiveButtonCaption, String negativeButtonCaption) {
        Bundle args = new Bundle(4);

        ActionBottomSheetDialogFragment fragment = new ActionBottomSheetDialogFragment();

        args.putString(ARG_TITLE, title);
        args.putString(ARG_MESSAGE, message);
        args.putString(ARG_POSITIVE_BUTTON_CAPTION, positiveButtonCaption);
        args.putString(ARG_NEGATIVE_BUTTON_CAPTION, negativeButtonCaption);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dialogsEventBus = getCompositionRoot().getDialogsEventBus();

        if (getArguments() == null) {
            throw new IllegalArgumentException("arguments mustn't be null!");
        }

        title = getArguments().getString(ARG_TITLE);
        message = getArguments().getString(ARG_MESSAGE);
        positiveButtonCaption = getArguments().getString(ARG_POSITIVE_BUTTON_CAPTION);
        negativeButtonCaption = getArguments().getString(ARG_NEGATIVE_BUTTON_CAPTION);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new BottomSheetDialog(requireContext());
        dialog.setContentView(R.layout.fragment_action_dialog);

        TextView titleTextView = dialog.findViewById(R.id.title_textview);
        TextView messageTextView = dialog.findViewById(R.id.message_textview);
        MaterialButton positiveButton = dialog.findViewById(R.id.positive_button);
        MaterialButton negativeButton = dialog.findViewById(R.id.negative_button);

        titleTextView.setText(title);
        messageTextView.setText(message);
        positiveButton.setText(positiveButtonCaption);
        negativeButton.setText(negativeButtonCaption);

        positiveButton.setOnClickListener(view -> onPositiveButtonClicked());

        negativeButton.setOnClickListener(view -> onNegativeButtonClicked());

        return dialog;
    }

    @Override
    public void onPositiveButtonClicked() {
        dialogsEventBus.postEvent(new ActionDialogEvent(ActionDialogEvent.Button.POSITIVE));
        dismiss();
    }

    @Override
    public void onNegativeButtonClicked() {
        dialogsEventBus.postEvent(new ActionDialogEvent(ActionDialogEvent.Button.NEGATIVE));
        dismiss();
    }
}

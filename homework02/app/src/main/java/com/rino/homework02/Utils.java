package com.rino.homework02;

import android.view.View;
import android.widget.Button;

public class Utils {

    public static void setOnClickListenerForManyButtons(View.OnClickListener onClickListener, Button... buttons) {
        for (Button button : buttons) {
            button.setOnClickListener(onClickListener);
        }
    }

}

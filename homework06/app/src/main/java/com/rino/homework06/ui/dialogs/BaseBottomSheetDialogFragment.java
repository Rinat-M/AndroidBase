package com.rino.homework06.ui.dialogs;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.rino.homework06.common.di.ControllerCompositionRoot;
import com.rino.homework06.ui.activities.MainActivity;

public class BaseBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private ControllerCompositionRoot controllerCompositionRoot;

    protected ControllerCompositionRoot getCompositionRoot() {
        if (controllerCompositionRoot == null) {
            controllerCompositionRoot = new ControllerCompositionRoot(
                    ((MainActivity) requireActivity()).getActivityCompositionRoot()
            );
        }

        return controllerCompositionRoot;
    }

}

package com.rino.homework06.ui.controllers;

import androidx.fragment.app.Fragment;

import com.rino.homework06.common.di.ControllerCompositionRoot;
import com.rino.homework06.ui.activities.MainActivity;

public class BaseFragment extends Fragment {

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

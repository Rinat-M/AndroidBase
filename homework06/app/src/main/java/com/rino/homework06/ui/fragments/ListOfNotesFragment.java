package com.rino.homework06.ui.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.rino.homework06.R;
import com.rino.homework06.common.StateStore;
import com.rino.homework06.common.datasources.NotesSource;
import com.rino.homework06.ui.adapters.NotesAdapter;
import com.rino.homework06.ui.controllers.BaseFragment;
import com.rino.homework06.ui.dialogs.DialogsEventBus;
import com.rino.homework06.ui.dialogs.DialogsManager;
import com.rino.homework06.ui.dialogs.actiondialog.ActionDialogEvent;
import com.rino.homework06.ui.dialogs.notedialog.NoteDialogEvent;
import com.rino.homework06.ui.navigation.ScreenNavigator;

import java.util.Objects;

public class ListOfNotesFragment extends BaseFragment implements DialogsEventBus.Listener {
    public static final String LIST_OF_NOTES_FRAGMENT_TAG = "LIST_OF_NOTES_FRAGMENT_TAG";
    private static final int DEFAULT_ANIMATION_DURATION = 250;

    private NotesSource dataSource;
    private ScreenNavigator screenNavigator;
    private StateStore stateStore;
    private DialogsManager dialogsManager;
    private DialogsEventBus dialogsEventBus;

    private NotesAdapter notesAdapter;
    private RecyclerView recyclerView;

    public static ListOfNotesFragment newInstance() {
        return new ListOfNotesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        dataSource = getCompositionRoot().getDataSource();
        screenNavigator = getCompositionRoot().getScreenNavigator();
        stateStore = getCompositionRoot().getStateStore();
        dialogsManager = getCompositionRoot().getDialogsManager();
        dialogsEventBus = getCompositionRoot().getDialogsEventBus();
    }

    @Override
    public void onStart() {
        super.onStart();
        dialogsEventBus.registerListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_of_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navigateToFragment();

        initRecyclerView(view);

        dataSource.fetchData(notesSource -> notesAdapter.notifyDataSetChanged());
    }

    private void navigateToFragment() {
        switch (screenNavigator.getCurrentFragmentEntry()) {
            case NOTE:
                dialogsManager.showAddOrEditNoteDialog(null, stateStore.getSelectedPosition());
                break;
            case ABOUT:
                screenNavigator.toAboutScreen();
                break;
        }
    }

    private void initRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.list_of_notes_recyclerview);
        recyclerView.setHasFixedSize(true);

        addItemDecoration();

        setItemAnimator();

        setAdapter();
    }

    private void addItemDecoration() {
        DividerItemDecoration itemDecoration = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);

        Drawable drawable = Objects.requireNonNull(AppCompatResources.getDrawable(requireContext(), R.drawable.separator));
        itemDecoration.setDrawable(drawable);

        recyclerView.addItemDecoration(itemDecoration);
    }

    private void setItemAnimator() {
        DefaultItemAnimator animator = new DefaultItemAnimator();

        animator.setAddDuration(DEFAULT_ANIMATION_DURATION);
        animator.setRemoveDuration(DEFAULT_ANIMATION_DURATION);
        animator.setChangeDuration(DEFAULT_ANIMATION_DURATION);

        recyclerView.setItemAnimator(animator);
    }

    private void setAdapter() {
        notesAdapter = new NotesAdapter();

        notesAdapter.setDataSource(dataSource);
        notesAdapter.setOnItemClickListener((v, position) -> dialogsManager.showAddOrEditNoteDialog(null, position));
        notesAdapter.setRegisterContextMenuHandler(this::registerForContextMenu);

        recyclerView.setAdapter(notesAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int menuId = item.getItemId();

        if (menuId == R.id.action_add) {
            dialogsManager.showAddOrEditNoteDialog(null, null);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = requireActivity().getMenuInflater();
        menuInflater.inflate(R.menu.notes_list_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int position = notesAdapter.getMenuPosition();

        if (item.getItemId() == R.id.action_delete) {
            stateStore.setPositionToDelete(position);
            dialogsManager.showDeleteActionDialog(null);
            return true;
        }

        return super.onContextItemSelected(item);
    }

    @Override
    public void onDialogEvent(Object event) {
        if (event instanceof ActionDialogEvent) {
            switch (((ActionDialogEvent) event).getClickedButton()) {
                case POSITIVE:
                    int position = stateStore.getPositionToDelete();
                    dataSource.deleteNote(position);
                    notesAdapter.notifyItemRemoved(position);
                    break;
                case NEGATIVE:
                    stateStore.resetPositionToDelete();
                    break;
            }
        } else if (event instanceof NoteDialogEvent) {
            switch (((NoteDialogEvent) event).getClickedButton()) {
                case POSITIVE:
                    dataSource.fetchData(notesSource -> notesAdapter.notifyDataSetChanged());
                    break;
                case NEGATIVE:
                    break;
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        dialogsEventBus.unregisterListener(this);
    }
}
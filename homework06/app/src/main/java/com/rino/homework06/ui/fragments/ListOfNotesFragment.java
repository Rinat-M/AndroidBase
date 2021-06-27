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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.rino.homework06.R;
import com.rino.homework06.common.datasources.NotesSource;
import com.rino.homework06.common.entities.Note;
import com.rino.homework06.common.entities.Priority;
import com.rino.homework06.ui.adapters.NotesAdapter;

import java.util.Objects;

public class ListOfNotesFragment extends Fragment {
    public static final String LIST_OF_NOTES_FRAGMENT_TAG = "LIST_OF_NOTES_FRAGMENT_TAG";
    private static final int DEFAULT_ANIMATION_DURATION = 250;

    private OnItemSelectedListener onItemSelectedListener;
    private NotesSource dataSource;
    private NotesAdapter notesAdapter;
    private RecyclerView recyclerView;

    public static ListOfNotesFragment newInstance() {
        return new ListOfNotesFragment();
    }

    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_of_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dataSource = new NotesSourceImpl();

        initRecyclerView(view);
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
        notesAdapter = new NotesAdapter(dataSource);

        NotesAdapter adapter = new NotesAdapter(dataSource);
        adapter.setOnItemClickListener((v, position) -> {
            Note note = dataSource.getNote(position);
            onItemSelectedListener.onItemSelected(note);
        });

        notesAdapter.setRegisterContextMenuHandler(this::registerForContextMenu);

        recyclerView.setAdapter(notesAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int menuId = item.getItemId();

        if (menuId == R.id.action_add) {
            dataSource.addNote(new Note("test", "Test", new Date(), Priority.NORMAL));
            notesAdapter.notifyItemInserted(dataSource.getSize() - 1);
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
            dataSource.deleteNote(position);
            notesAdapter.notifyItemRemoved(position);
            return true;
        }

        return super.onContextItemSelected(item);
    }
}
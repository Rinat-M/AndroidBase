package com.rino.homework06.ui.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.rino.homework06.R;
import com.rino.homework06.core.datasources.NotesSource;
import com.rino.homework06.core.datasources.NotesSourceImpl;
import com.rino.homework06.core.entities.Note;
import com.rino.homework06.core.listeners.OnItemSelectedListener;
import com.rino.homework06.ui.adapters.NotesAdapter;

import java.util.Objects;

public class ListOfNotesFragment extends Fragment {
    public static final String LIST_OF_NOTES_FRAGMENT_TAG = "LIST_OF_NOTES_FRAGMENT_TAG";

    private OnItemSelectedListener onItemSelectedListener;
    private NotesSource dataSource;

    public static ListOfNotesFragment newInstance() {
        return new ListOfNotesFragment();
    }

    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
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
        RecyclerView recyclerView = view.findViewById(R.id.list_of_notes_recyclerview);
        recyclerView.setHasFixedSize(true);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL);

        Drawable drawable = Objects.requireNonNull(AppCompatResources.getDrawable(view.getContext(), R.drawable.separator));
        itemDecoration.setDrawable(drawable);

        recyclerView.addItemDecoration(itemDecoration);

        NotesAdapter adapter = new NotesAdapter(dataSource);
        adapter.setOnItemClickListener((v, position) -> {
            Note note = dataSource.getNote(position);
            onItemSelectedListener.onItemSelected(note);
        });

        recyclerView.setAdapter(adapter);
    }
}
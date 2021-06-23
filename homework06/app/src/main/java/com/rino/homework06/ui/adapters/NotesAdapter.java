package com.rino.homework06.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.rino.homework06.R;
import com.rino.homework06.core.datasources.NotesSource;
import com.rino.homework06.core.entities.Note;
import com.rino.homework06.core.entities.Priority;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {
    private final NotesSource dataSource;

    private OnItemClickListener itemClickListener;

    public NotesAdapter(NotesSource dataSource) {
        this.dataSource = dataSource;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_of_notes_item, parent, false);

        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.bind(dataSource.getNote(position));
    }

    @Override
    public int getItemCount() {
        return dataSource.getSize();
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @FunctionalInterface
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleTextView;
        private final View priorityView;
        private final TextView createdAtTextView;

        private final int priorityHighColor;
        private final int priorityNormalColor;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.title);
            priorityView = itemView.findViewById(R.id.priority);
            createdAtTextView = itemView.findViewById(R.id.created_at);

            priorityHighColor = ContextCompat.getColor(itemView.getContext(), R.color.red_500);
            priorityNormalColor = ContextCompat.getColor(itemView.getContext(), R.color.white);

            ConstraintLayout constraint = itemView.findViewById(R.id.note_item_container);
            constraint.setOnClickListener(view -> itemClickListener.onItemClick(itemView, getAdapterPosition()));
        }

        public void bind(Note note) {
            titleTextView.setText(note.getTitle());
            createdAtTextView.setText(note.getCreatedAtInFormat());
            setPriorityColor(note);
        }

        private void setPriorityColor(Note note) {
            if (note.getPriority() == Priority.HIGH) {
                priorityView.setBackgroundColor(priorityHighColor);
            } else {
                priorityView.setBackgroundColor(priorityNormalColor);
            }
        }
    }
}

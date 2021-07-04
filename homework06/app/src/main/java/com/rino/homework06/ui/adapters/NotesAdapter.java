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
import com.rino.homework06.common.datasources.NotesSource;
import com.rino.homework06.common.entities.Note;
import com.rino.homework06.common.entities.Priority;
import com.rino.homework06.common.handlers.RegisterContextMenuHandler;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {
    private NotesSource dataSource;

    private OnItemClickListener itemClickListener;
    private RegisterContextMenuHandler registerContextMenuHandler;

    private int menuPosition;

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

    public void setDataSource(NotesSource dataSource) {
        this.dataSource = dataSource;
        notifyDataSetChanged();
    }

    @FunctionalInterface
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setRegisterContextMenuHandler(RegisterContextMenuHandler registerContextMenuHandler) {
        this.registerContextMenuHandler = registerContextMenuHandler;
    }

    public int getMenuPosition() {
        return menuPosition;
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
            constraint.setOnClickListener(view -> {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(itemView, getAdapterPosition());
                }
            });

            if (registerContextMenuHandler != null) {
                registerContextMenuHandler.registerContextMenu(constraint);
            }

            constraint.setOnLongClickListener(view -> {
                menuPosition = getLayoutPosition();
                return itemView.showContextMenu();
            });
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

package com.rino.homework06.common.datasources;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.rino.homework06.common.entities.Note;
import com.rino.homework06.common.handlers.FetchDataCompletedHandler;
import com.rino.homework06.common.mapping.NoteMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class NotesFirebaseImpl implements NotesSource {

    private static final String NOTES_COLLECTION = "notes";
    private static final String TAG = "NotesFirebaseImpl";

    private final FirebaseFirestore store = FirebaseFirestore.getInstance();
    private final CollectionReference collection = store.collection(NOTES_COLLECTION);

    private List<Note> notes = new ArrayList<>();

    @Override
    public void fetchData(FetchDataCompletedHandler fetchDataCompletedHandler) {
        collection
                .orderBy(NoteMapping.Fields.CREATED_AT, Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        notes = new ArrayList<>();

                        Objects.requireNonNull(task.getResult());

                        for (QueryDocumentSnapshot snapshot : task.getResult()) {
                            Map<String, Object> document = snapshot.getData();
                            String id = snapshot.getId();

                            Note note = NoteMapping.toNote(id, document);
                            notes.add(note);
                        }

                        Log.d(TAG, "Fetch data was successful! Notes count: " + notes.size());

                        fetchDataCompletedHandler.fetchDataCompleted(this);
                    } else {
                        Log.d(TAG, "An error occurred while fetching data from the Firestore", task.getException());
                    }
                })
                .addOnFailureListener(e -> Log.e(TAG, "An error occurred while fetching data from the Firestore", e));
    }

    @Override
    public Note getNote(int position) {
        return notes.get(position);
    }

    @Override
    public int getSize() {
        if (notes == null) {
            return 0;
        }

        return notes.size();
    }

    @Override
    public void deleteNote(int position) {
        String positionId = notes.get(position).getId();
        collection.document(positionId).delete();
        notes.remove(position);
    }

    @Override
    public void addNote(Note note) {
        collection
                .add(NoteMapping.toDocument(note))
                .addOnSuccessListener(documentReference -> note.setId(documentReference.getId()));
    }

    @Override
    public void updateNote(int position, Note note) {
        String positionId = note.getId();
        collection.document(positionId).set(NoteMapping.toDocument(note));
    }

    @Override
    public void deleteAll() {
        for (Note note : notes) {
            collection.document(note.getId()).delete();
        }

        notes.clear();
    }
}

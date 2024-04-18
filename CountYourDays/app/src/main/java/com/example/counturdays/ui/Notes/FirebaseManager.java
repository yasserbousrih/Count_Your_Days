package com.example.counturdays.ui.Notes;


import android.annotation.SuppressLint;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FirebaseManager {
    private static FirebaseManager firebaseManager;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mNotesRef;

    @SuppressLint("SimpleDateFormat")
    private static final DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

    private FirebaseManager() {
        mDatabase = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            mNotesRef = mDatabase.getReference().child("notes").child(user.getUid());
        }
    }

    public static FirebaseManager getInstance() {
        if (firebaseManager == null) {
            firebaseManager = new FirebaseManager();
        }
        return firebaseManager;
    }

    public String addNoteToDatabase(String userId, String title, String desc) {
        DatabaseReference userNotesRef = FirebaseDatabase.getInstance().getReference().child("user_notes").child(userId);
        String noteId = userNotesRef.push().getKey();
        Note newNote = new Note(noteId, title, desc);
        userNotesRef.child(noteId).setValue(newNote);
        return noteId;
    }

    public void deleteNoteFromDB(Note note) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DatabaseReference notesRef = FirebaseDatabase.getInstance().getReference().child("user_notes").child(userId).child(note.getId());
            notesRef.removeValue();
        }
    }


    public void populateNoteListArray(String userId, final FirebaseCallback callback) {
        DatabaseReference userNotesRef = FirebaseDatabase.getInstance().getReference().child("user_notes").child(userId);
        userNotesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Note.noteArrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Note note = snapshot.getValue(Note.class);
                    Note.noteArrayList.add(note);
                }
                callback.onCallback();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FirebaseManager", "Failed to read value.", databaseError.toException());
            }
        });
    }

    public void updateNoteInDB(Note note) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DatabaseReference notesRef = FirebaseDatabase.getInstance().getReference().child("user_notes").child(userId).child(note.getId());
            notesRef.setValue(note);
        }
    }


    private String getStringFromDate(Date date) {
        if (date == null) return null;
        return dateFormat.format(date);
    }

    private Date getDateFromString(String string) {
        try {
            return dateFormat.parse(string);
        } catch (Exception e) {
            return null;
        }
    }

    public interface FirebaseCallback {
        void onCallback();


    }
}

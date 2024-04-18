package com.example.counturdays.ui.Notes;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.counturdays.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class NotesFragment extends Fragment {

    private ListView noteListView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notes_fragment, container, false);
        initWidgets(view);
        setNoteAdapter();
        setOnClickListener(view);
        return view;
    }

    private void initWidgets(View view) {
        noteListView = view.findViewById(R.id.noteListView);
        noteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Note selectedNote = (Note) parent.getItemAtPosition(position);
                if (selectedNote == null) {
                    Toast.makeText(requireContext(), "Adding a new note", Toast.LENGTH_SHORT).show();
                    newNote();
                } else {
                    openNoteDetail(selectedNote.getId());
                }
            }
        });
    }


    private void openNoteDetail(String noteId) {
        Intent intent = new Intent(requireContext(), NoteDetailActivity.class);
        intent.putExtra(Note.NOTE_EDIT_EXTRA, noteId);
        startActivity(intent);
    }

    private void setNoteAdapter() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            FirebaseManager.getInstance().populateNoteListArray(userId, new FirebaseManager.FirebaseCallback() {
                @Override
                public void onCallback() {
                    NoteAdapter noteAdapter = new NoteAdapter(requireContext(), Note.nonDeletedNotes());
                    noteListView.setAdapter(noteAdapter);
                    noteAdapter.updateList(Note.nonDeletedNotes());
                }


            });
        }
    }


    private void setOnClickListener(View view) {
        Button newNoteButton = view.findViewById(R.id.newNoteButton);
        newNoteButton.setOnClickListener(v -> newNote());
    }

    private void newNote() {
        Intent newNoteIntent = new Intent(requireActivity(), NoteDetailActivity.class);
        startActivity(newNoteIntent);
    }

    @Override
    public void onResume() {
        super.onResume();
        setNoteAdapter();
    }
}

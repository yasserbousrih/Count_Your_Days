package com.example.counturdays.ui.Notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.counturdays.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Date;

public class NoteDetailActivity extends AppCompatActivity {
    private EditText titleEditText, descEditText;
    private Button deleteButton;
    private Note selectedNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
        initWidgets();
        checkForEditNote();
    }

    private void initWidgets() {
        titleEditText = findViewById(R.id.titleEditText);
        descEditText = findViewById(R.id.descriptionEditText);
        deleteButton = findViewById(R.id.deleteNoteButton);
    }

    private void checkForEditNote() {
        Intent previousIntent = getIntent();

        String passedNoteID = previousIntent.getStringExtra(Note.NOTE_EDIT_EXTRA);
        selectedNote = Note.getNoteForID(passedNoteID);

        if (selectedNote != null) {
            titleEditText.setText(selectedNote.getTitle());
            descEditText.setText(selectedNote.getDescription());
            deleteButton.setVisibility(View.VISIBLE);
        } else {
            deleteButton.setVisibility(View.INVISIBLE);
        }
    }

    public void saveNote(View view) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            String title = titleEditText.getText().toString();
            String desc = descEditText.getText().toString();

            if (selectedNote == null) {

                String noteId = FirebaseManager.getInstance().addNoteToDatabase(userId, title, desc);
                if (noteId != null) {
                    Note newNote = new Note(noteId, title, desc);
                    Note.noteArrayList.add(newNote);
                    showToast("Note saved");
                } else {
                    showToast("Failed to save note");
                }
            } else {
                selectedNote.setTitle(title);
                selectedNote.setDescription(desc);
                FirebaseManager.getInstance().updateNoteInDB(selectedNote);
                showToast("Note updated");
            }
        }
        finish();
    }


    public void deleteNote(View view) {
        if (selectedNote != null) {
            FirebaseManager.getInstance().deleteNoteFromDB(selectedNote);
            showToast("Note deleted");
        }
        finish();
    }


    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}


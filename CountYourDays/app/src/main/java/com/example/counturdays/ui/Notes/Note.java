package com.example.counturdays.ui.Notes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Note {
    public static final String NOTE_EDIT_EXTRA = "NOTE_EDIT_EXTRA"; // Replace with your actual key
    static ArrayList<Note> noteArrayList = new ArrayList<>();

    private String id;
    private String title;
    private String description;
    private Date deleted;

    public Note() {

    }

    public Note(String id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public static List<Note> nonDeletedNotes() {
        List<Note> nonDeletedNotes = new ArrayList<>();
        for (Note note : noteArrayList) {

            if (!note.getDescription().isEmpty()) {
                nonDeletedNotes.add(note);
            }
        }
        return nonDeletedNotes;
    }

    public static Note getNoteForID(String passedNoteID) {
        for (Note note : noteArrayList) {
            if (note.getId().equals(passedNoteID)) {
                return note;
            }
        }
        return null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDeleted(Date date) {
        this.deleted = date;
    }

}

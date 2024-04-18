package com.example.counturdays.ui.Notes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.counturdays.R;

import java.util.List;

public class NoteAdapter extends ArrayAdapter<Note> {
    private List<Note> notes;

    public NoteAdapter(Context context, List<Note> notes) {
        super(context, 0, notes);
        this.notes = notes;
    }

    public void updateList(List<Note> notes) {
        clear();
        addAll(notes);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Note note = getItem(position);
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.note_cell, parent, false);

        TextView title = convertView.findViewById(R.id.cellTitle);
        TextView desc = convertView.findViewById(R.id.cellDesc);

        if (note != null) {
            title.setText(note.getTitle());
            desc.setText(note.getDescription());
        }

        return convertView;
    }
}

package com.example.counturdays.ui.Tasks;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.counturdays.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.nullness.qual.NonNull;

public class AddTaskDialogFragment extends DialogFragment {
    private EditText editTextTaskName;
    private Button btnSaveTask;

    private Task taskToUpdate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_save_task, container, false);

        editTextTaskName = view.findViewById(R.id.editTextTaskName);
        btnSaveTask = view.findViewById(R.id.btnSaveTask);

        if (taskToUpdate != null) {
            editTextTaskName.setText(taskToUpdate.getTaskName());
            btnSaveTask.setText("Update Task");
        }

        btnSaveTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTask();
            }
        });

        return view;
    }


    public void setTaskToUpdate(Task task) {
        taskToUpdate = task;
    }

    private void saveTask() {
        String taskName = editTextTaskName.getText().toString();
        if (!TextUtils.isEmpty(taskName)) {
            FirebaseTaskHelper firebaseTaskHelper = FirebaseTaskHelper.getInstance();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                String userId = user.getUid();
                if (taskToUpdate != null) {
                    taskToUpdate.setTaskName(taskName);
                    firebaseTaskHelper.updateTaskInDB(taskToUpdate);
                } else {
                    firebaseTaskHelper.addTaskToDatabase(userId, taskName, false);
                }
            }
            dismiss();
        } else {
            editTextTaskName.setError("Task name cannot be empty");
        }
    }
}
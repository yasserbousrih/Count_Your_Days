package com.example.counturdays.ui.Tasks;

import static com.example.counturdays.ui.Tasks.Task.taskArrayList;

import android.util.Log;

import com.example.counturdays.ui.Tasks.TaskAdapter;

import com.example.counturdays.ui.Notes.FirebaseManager;
import com.example.counturdays.ui.Notes.Note;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseTaskHelper {
    private static FirebaseTaskHelper firebaseTaskHelper;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mTasksRef;

    private ArrayList<Task> fetchedTasks = new ArrayList<>();


    private FirebaseTaskHelper() {
        mDatabase = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            mTasksRef = mDatabase.getReference().child("tasks").child(user.getUid());
        }
    }

    public static FirebaseTaskHelper getInstance() {
        if (firebaseTaskHelper == null) {
            firebaseTaskHelper = new FirebaseTaskHelper();
        }
        return firebaseTaskHelper;
    }

    public String addTaskToDatabase(String userId, String taskName, boolean isCompleted) {
        DatabaseReference userTasksRef = FirebaseDatabase.getInstance().getReference().child("user_tasks").child(userId);
        String taskId = userTasksRef.push().getKey();
        Task newTask = new Task(taskId, taskName, isCompleted);
        userTasksRef.child(taskId).setValue(newTask);
        return taskId;
    }

    public void deleteTaskFromDB(Task task) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DatabaseReference tasksRef = FirebaseDatabase.getInstance().getReference().child("user_tasks").child(userId).child(task.getTaskId());
            tasksRef.removeValue();
        }
    }

    public interface FirebaseCallback {
        void onCallback(List<Task> taskList);
    }

    public void populateTaskListArray(String userId, final FirebaseCallback callback) {
        DatabaseReference userTasksRef = FirebaseDatabase.getInstance().getReference().child("user_tasks").child(userId);
        userTasksRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fetchedTasks.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Task task = snapshot.getValue(Task.class);
                    fetchedTasks.add(task);
                }

                callback.onCallback(fetchedTasks);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FirebaseManager", "Failed to read value.", databaseError.toException());
            }
        });
    }


    public ArrayList<Task> getFetchedTasks() {
        return fetchedTasks;
    }

    public void updateTaskInDB(Task task) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DatabaseReference tasksRef = FirebaseDatabase.getInstance().getReference().child("user_tasks").child(userId).child(task.getTaskId());
            tasksRef.setValue(task); // Update the task in the database
        }
    }


}

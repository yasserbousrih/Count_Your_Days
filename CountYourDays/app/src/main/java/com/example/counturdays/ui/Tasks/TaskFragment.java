package com.example.counturdays.ui.Tasks;

import static com.example.counturdays.ui.Tasks.Task.taskArrayList;

import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.counturdays.R;
import com.example.counturdays.ui.Notes.FirebaseManager;
import com.example.counturdays.ui.Notes.Note;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.counturdays.ui.Tasks.TaskAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class TaskFragment extends Fragment {

    private RecyclerView recyclerViewTasks;
    private TaskAdapter taskAdapter;
    private List<Task> tasks;
    private DatabaseReference tasksRef;
    private Button btnAddTask;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task, container, false);

        recyclerViewTasks = view.findViewById(R.id.recyclerViewTasks);
        recyclerViewTasks.setLayoutManager(new LinearLayoutManager(getContext()));

        tasks = new ArrayList<>();

        taskAdapter = new TaskAdapter(tasks);
        recyclerViewTasks.setAdapter(taskAdapter);


        taskAdapter = new TaskAdapter(tasks);
        recyclerViewTasks.setAdapter(taskAdapter);

        tasksRef = FirebaseDatabase.getInstance().getReference("tasks");

        btnAddTask = view.findViewById(R.id.btnAddTask);
        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddTaskDialogFragment dialog = new AddTaskDialogFragment();
                dialog.show(getParentFragmentManager(), "AddTaskDialogFragment");
            }
        });

        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Task task = tasks.get(position);

                FirebaseTaskHelper firebaseTaskHelper = FirebaseTaskHelper.getInstance();

                if (direction == ItemTouchHelper.LEFT) {
                    firebaseTaskHelper.deleteTaskFromDB(task);
                } else if (direction == ItemTouchHelper.RIGHT) {
                    AddTaskDialogFragment dialogFragment = new AddTaskDialogFragment();
                    dialogFragment.setTaskToUpdate(task);
                    dialogFragment.show(requireActivity().getSupportFragmentManager(), "AddTaskDialog");
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive).addSwipeLeftBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent)).addSwipeLeftActionIcon(R.drawable.ic_baseline_delete).addSwipeRightBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorText)).addSwipeRightActionIcon(R.drawable.ic_baseline_edit).create().decorate();

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerViewTasks);

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

        FirebaseTaskHelper firebaseTaskHelper = FirebaseTaskHelper.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();

            firebaseTaskHelper.populateTaskListArray(userId, new FirebaseTaskHelper.FirebaseCallback() {
                @Override
                public void onCallback(List<Task> taskList) {
                    tasks.clear();
                    tasks.addAll(taskList);

                    Collections.sort(tasks, new TaskAdapter.TaskComparator());

                    taskAdapter.notifyDataSetChanged();
                }
            });
        }
    }


}

package com.example.counturdays.ui.Tasks;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.counturdays.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.counturdays.ui.Tasks.TaskFragment;

import java.util.Comparator;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private List<Task> tasks;

    public TaskAdapter(List<Task> tasks) {
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(view, this); // Pass the TaskAdapter instance to the TaskViewHolder constructor
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.bind(task);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        private TextView taskNameTextView;
        private CheckBox taskCheckBox;
        private TaskAdapter taskAdapter;

        public TaskViewHolder(@NonNull View itemView, TaskAdapter taskAdapter) {
            super(itemView);
            this.taskAdapter = taskAdapter;
            taskNameTextView = itemView.findViewById(R.id.taskNameTextView);
            taskCheckBox = itemView.findViewById(R.id.taskCheckBox);


            taskCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Task task = taskAdapter.tasks.get(position);
                        task.setCompleted(isChecked);

                        FirebaseTaskHelper firebaseTaskHelper = FirebaseTaskHelper.getInstance();
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            String userId = user.getUid();

                            firebaseTaskHelper.updateTaskInDB(task);

                        }
                    }
                }
            });


        }

        public void bind(Task task) {
            taskNameTextView.setText(task.getTaskName());
            taskCheckBox.setChecked(task.isCompleted());
        }
    }

    // Inside TaskAdapter class
    public static class TaskComparator implements Comparator<Task> {
        @Override
        public int compare(Task task1, Task task2) {
            // Completed tasks should come after uncompleted tasks
            if (task1.isCompleted() && !task2.isCompleted()) {
                return 1;
            } else if (!task1.isCompleted() && task2.isCompleted()) {
                return -1;
            } else {
                // If both tasks have the same completion status, compare their names
                return task1.getTaskName().compareTo(task2.getTaskName());
            }
        }
    }

}

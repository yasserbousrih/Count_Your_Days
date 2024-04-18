package com.example.counturdays.ui.Tasks;

import com.example.counturdays.ui.Notes.Note;

import java.util.ArrayList;

public class Task {

    private String taskId;
    private String taskName;
    private boolean isCompleted;
    static ArrayList<Task> taskArrayList = new ArrayList<>();


    public Task() {
    }


    public Task(String taskId, String taskName, boolean isCompleted) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.isCompleted = isCompleted;
    }


    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}

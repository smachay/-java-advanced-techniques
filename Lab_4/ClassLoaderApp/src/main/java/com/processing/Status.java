package com.processing;

public class Status {

    private String taskName;
    private int progress;

    public String getTaskName() {
        return taskName;
    }

    public int getProgress() {
        return progress;
    }

    public Status(String taskName, int progress) {
        super();
        this.taskName = taskName;
        this.progress = progress;
    }

}
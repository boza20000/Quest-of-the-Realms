package com.questoftherealm.expeditions;

public class Mission {
    private String name;
    private String task;
    private boolean completed;

    public Mission(String name, String task) {
        this.name = name;
        this.task = task;
        this.completed = false;
    }

    public String getName() {
        return name;
    }

    public String getTask() {
        return task;
    }
}

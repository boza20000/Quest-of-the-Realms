package com.questoftherealm.expeditions;

public abstract class Mission {
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

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    protected void complete() {
        setCompleted(true);
        System.out.println("✅ Mission completed: " + name);
    }

    public abstract boolean checkCompletion();

}

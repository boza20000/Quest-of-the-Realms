package com.questoftherealm.expeditions;

import java.util.List;

public abstract class Quest {
    private List<Mission> missions;
    private String name;
    private String description;
    private static boolean completed = false;

    public Quest(String name, List<Mission> missions, String description) {
        this.missions = missions;
        this.description = description;
        this.name = name;
    }

    public static boolean isCompleted() {
        return completed;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Mission> getMissions() {
        return missions;
    }

    public void updateStatus() {
        boolean isAllReady = true;
        if (this.isCompleted()) return;
        for (Mission m : this.getMissions()) {
            if (!m.isCompleted()) {
                m.checkCompletion();
                isAllReady = false;
            }
        }
        if (isAllReady) {
            this.setCompleted(true);
            System.out.println("You have completed this quest successfully");
        }
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void listMissions() {
        for (Mission mission : missions) {
            System.out.println("Missions: " + mission.getName());
        }
    }

}

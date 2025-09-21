package com.questoftherealm.expeditions;

import java.util.List;

public class Quest {
    private List<Mission> missions;
    private String name;
    private String description;
    private boolean completed;

    public Quest(String name, List<Mission> missions, String description) {
        this.missions = missions;
        this.description = description;
        this.name = name;
    }

    public boolean isCompleted() {
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

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void listMissions() {
        for (Mission mission : missions) {
            System.out.println("Missions: " + mission.getName());
        }
    }

}

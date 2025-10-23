package com.questoftherealm.expeditions;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.questoftherealm.expeditions.quests.*;
import java.util.List;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = StartQuest.class, name = "StartQuest"),
        @JsonSubTypes.Type(value = NorthExploration.class, name = "NorthExploration"),
        @JsonSubTypes.Type(value = GoblinAmbush.class, name = "GoblinAmbush"),
        @JsonSubTypes.Type(value = RiseOfTheGoblinThreat.class, name = "RiseOfTheGoblinThreat"),
        @JsonSubTypes.Type(value = FinalBattle.class, name = "FinalBattle")
})

public abstract class Quest {
    private final List<Mission> missions;
    private final String name;
    private final String description;
    private boolean completed = false;

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

    public void updateStatus() {
        boolean isAllReady = true;
        if (this.isCompleted()) return;
        for (Mission m : this.getMissions()) {
            m.checkCompletion();
            if (!m.isCompleted()) {
                isAllReady = false;
            }
        }
        if (isAllReady) {
            this.setCompleted(true);
            QuestFactory.nextQuest();
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

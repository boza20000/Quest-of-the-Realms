package com.questoftherealm.expeditions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.quests.*;
import com.questoftherealm.game.GameState;

import java.util.List;
import java.util.Objects;

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
    @JsonIgnore
    private Player player;

    public Quest(String name, List<Mission> missions, String description,Player player) {
        this.missions = missions;
        this.description = description;
        this.name = name;
        this.player = player;
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

    public Player getPlayer() {
        return player;
    }

    public List<Mission> getMissions() {
        return missions;
    }

    public void updateStatus(GameState state) {
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
            player.getQuestFactory().nextQuest(state);
            state.getGameServices().getOutput().println("You have completed this quest successfully");
        }
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Quest q)) return false;
        return Objects.equals(this.getName(), q.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public void setPlayer(Player player) {
        this.player = player;
        for (Mission m :getMissions()){
            m.setPlayer(player);
        }
    }
}

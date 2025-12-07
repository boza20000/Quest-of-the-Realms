package com.questoftherealm.expeditions.missions;

import com.fasterxml.jackson.annotation.*;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.game.GameState;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)

public class Mission {
    private String name;
    private String task;
    private boolean completed;
    private Missions missionType;
    @JsonIgnore
    private GameState state;
    @JsonIgnore
    private Player player;


    protected Mission() {
    }

    @JsonCreator
    public Mission(
            @JsonProperty("name") String name,
            @JsonProperty("task") String task,
            @JsonProperty("completed") boolean completed,
            @JsonProperty("missionType") Missions missionType
    ) {
        this.name = name;
        this.task = task;
        this.completed = completed;
        this.missionType = missionType;
        this.player = null;
    }

    public Mission(Missions type, Player player, GameState state) {
        this.state = state;
        this.player = player;
        this.missionType = type;
        this.name = type.getNameKey(state);
        this.task = type.getDescriptionKey(state);
    }

    public Missions getMissionType() {
        return missionType;
    }

    public String getName() {
        return name;
    }

    public String getTask() {
        return task;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void setMissionType(Missions missionType) {
        this.missionType = missionType;
    }

    protected void complete() {
        setCompleted(true);
        state.getGameServices().getOutput().println("✅ " + state.getMessages().getBundle().get("mission.completed", name));
    }

    public void checkCompletion() {
        if (completed) return ;
        if (missionType.getCondition() != null && missionType.getCondition().check(player, this)) {
            complete();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Mission m)) return false;
        return Objects.equals(this.getName(), m.getName());
    }

    public void setState(GameState state) {
        this.state = state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}

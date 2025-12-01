package com.questoftherealm.expeditions;

import com.fasterxml.jackson.annotation.*;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.interfaces.MissionCondition;
import com.questoftherealm.expeditions.missions.*;
import com.questoftherealm.game.GameState;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Meet_the_Elder.class, name = "Meet_the_Elder"),
        @JsonSubTypes.Type(value = Travel_North.class, name = "Travel_North"),
        @JsonSubTypes.Type(value = Investigate_Northern_Villages.class, name = "Investigate_Northern_Villages"),
        @JsonSubTypes.Type(value = Gather_Supplies.class, name = "Gather_Supplies"),
        @JsonSubTypes.Type(value = Explore_Nearby_Forests.class, name = "Explore_Nearby_Forests"),
        @JsonSubTypes.Type(value = Infiltrate_the_Camp.class, name = "Infiltrate_the_Camp"),
        @JsonSubTypes.Type(value = Ambushed.class, name = "Ambushed"),
        @JsonSubTypes.Type(value = Escape_to_Safety.class, name = "Escape_to_Safety"),
        @JsonSubTypes.Type(value = Warn_the_Castle.class, name = "Warn_the_Castle"),
        @JsonSubTypes.Type(value = Assemble_an_Army.class, name = "Assemble_an_Army"),
        @JsonSubTypes.Type(value = Defeat_the_Goblin_General.class, name = "Defeat_the_Goblin_General"),
        @JsonSubTypes.Type(value = March_Into_the_Far_North.class, name = "March_Into_the_Far_North"),
        @JsonSubTypes.Type(value = Breach_the_Stronghold.class, name = "Breach_the_Stronghold"),
        @JsonSubTypes.Type(value = Defeat_the_Goblin_King.class, name = "Defeat_the_Goblin_King")
})
public abstract class Mission {
    protected String name;
    protected String task;
    protected boolean completed;
    @JsonIgnore
    protected Player player;
    @JsonIgnore
    protected MissionCondition condition;
    private MissionConditionType conditionType;
    @JsonIgnore
    private GameState state;

    public Mission(String name, String task, Player player, MissionCondition condition) {
        this.name = name;
        this.task = task;
        this.player = player;
        this.condition = condition;
        this.conditionType = MissionConditionFactory.getTypeForMission(this);
    }

    protected Mission() {
    }

    @JsonCreator
    public Mission(
            @JsonProperty("name") String name,
            @JsonProperty("task") String task,
            @JsonProperty("completed") boolean completed,
            @JsonProperty("conditionType") MissionConditionType conditionType
    ) {
        this.name = name;
        this.task = task;
        this.completed = completed;
        this.conditionType = conditionType;
        this.player = null;
        this.condition = null;
    }

    public MissionConditionType getConditionType() {
        return conditionType;
    }

    public void setCondition(MissionCondition condition) {
        this.condition = condition;
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

    protected void complete() {
        setCompleted(true);
        state.getGameServices().getOutput().println("✅ " + state.getMessages().getBundle().get("mission.completed", name));
    }

    public boolean checkCompletion() {
        if (completed) return true;
        if (condition != null && condition.check(player, this)) {
            complete();
            return true;
        }
        return false;
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

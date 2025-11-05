package com.questoftherealm.expeditions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.missions.*;
import java.util.Objects;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Meet_the_Elder.class, name = "Meet_the_Elder"),
        @JsonSubTypes.Type(value = Explore_the_Village.class, name = "Explore_the_Village"),
        @JsonSubTypes.Type(value = Travel_North.class, name = "Travel_North"),
        @JsonSubTypes.Type(value = Talk_To_Survivors.class, name = "Talk_To_Survivors"),
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
    private final String name;
    private final String task;
    private boolean completed;
    protected Player player;

    public Mission(String name, String task, Player player) {
        this.name = name;
        this.task = task;
        this.player = player;
    }

    @JsonCreator
    public Mission(
            @JsonProperty("name") String name,
            @JsonProperty("task") String task,
            @JsonProperty("completed") boolean completed
    ) {
        this.name = name;
        this.task = task;
        this.completed = completed;
        this.player = null;
    }

    public String getName() { return name; }
    public String getTask() { return task; }
    public Player getPlayer() { return player; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    protected void complete() {
        setCompleted(true);
        System.out.println("✅ Mission completed: " + name);
    }

    public abstract boolean checkCompletion();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Mission m)) return false;
        return Objects.equals(this.getName(), m.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}

package com.questoftherealm.expeditions;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.questoftherealm.expeditions.missions.*;

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
})

public abstract class Mission {
    private final String name;
    private final String task;
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

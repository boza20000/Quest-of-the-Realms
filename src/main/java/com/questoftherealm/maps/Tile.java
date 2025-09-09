package com.questoftherealm.maps;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Tile {
    private final TileTypes type;
    private final String description;
    private final boolean walkable;

    @JsonCreator
    public Tile( @JsonProperty("type")TileTypes type,@JsonProperty("description") String description,@JsonProperty("walkable") boolean walkable){
        this.type = type;
        this.description = description;
        this.walkable = walkable;
    }

    public TileTypes getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public boolean isWalkable() {
        return walkable;
    }
}

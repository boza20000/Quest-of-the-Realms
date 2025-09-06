package com.questoftherealm.maps;

public class Tile {
    private final TileTypes type;
    private final String description;
    private final boolean walkable;

    public Tile(TileTypes type, String description, boolean walkable) {
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

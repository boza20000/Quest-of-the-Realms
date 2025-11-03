package com.questoftherealm.map;

import java.util.concurrent.ThreadLocalRandom;

public enum Locations {
    ABANDONED_TOWER(new LocationData("Abandoned Tower", "A crumbling tower that once served as a lookout, now eerily silent.")),
    FORGOTTEN_RUINS(new LocationData("Forgotten Ruins", "The remains of a lost civilization, swallowed by time.")),
    CRUMBLING_WATCHTOWER(new LocationData("Crumbling Watchtower", "Once a proud guard post, now reduced to rubble.")),
    MAGES_TOWER(new LocationData("Mage’s Tower", "A spire where a sorcerer once dwelled, filled with lingering magic.")),
    IRON_MINE(new LocationData("Iron Mine", "A mine carved into the earth, rich in ore but plagued with danger.")),
    SHADOW_CAVERN(new LocationData("Shadow Cavern", "A yawning cave said to shelter creatures of the dark.")),
    SACRED_GROVE(new LocationData("Sacred Grove", "An ancient place, protected by nature’s magic.")),
    CRYSTAL_LAKE(new LocationData("Crystal Lake", "Shimmering waters that seem to hum with magical energy.")),
    BANDIT_CAMP(new LocationData("Bandit Camp", "A rough encampment of outlaws and thieves.")),
    SUNKEN_SWAMP(new LocationData("Sunken Swamp", "Rotting wetlands where the unwary often vanish.")),
    ANCIENT_ALTAR(new LocationData("Ancient Altar", "A stone altar covered in strange markings, radiating mystery.")),
    ABANDONED_HUT(new LocationData("Abandoned Hut", "A decrepit shack, its walls sagging with secrets long forgotten."));

    private final LocationData data;

    Locations(final LocationData data) {
        this.data = data;
    }

    public String getName(){
        return data.name();
    }

    public String getDescription(){
        return data.description();
    }

    public static Locations generateLocation(TileTypes type) {
        return switch (type) {
            case GRASS -> randomOf(Locations.SACRED_GROVE, Locations.ABANDONED_HUT, Locations.ANCIENT_ALTAR);
            case FOREST -> randomOf(Locations.FORGOTTEN_RUINS, Locations.CRUMBLING_WATCHTOWER, Locations.ABANDONED_TOWER);
            case SWAMP -> randomOf(Locations.SUNKEN_SWAMP, Locations.SHADOW_CAVERN, Locations.BANDIT_CAMP);
            case VILLAGE, CASTLE -> null;
            case MOUNTAIN -> randomOf(Locations.IRON_MINE, Locations.ABANDONED_TOWER, Locations.CRUMBLING_WATCHTOWER);
            case WATER -> randomOf(Locations.CRYSTAL_LAKE);
        };
    }

    private static Locations randomOf(Locations... options) {
        return options[ThreadLocalRandom.current().nextInt(options.length)];//returns random location
    }

    public static Locations getStructure(String structure) {
        if (structure == null || structure.isBlank()) {
            return null;
        }
        String formatted = structure.trim().toLowerCase();

        for (Locations l : Locations.values()) {
            if (l.name().equalsIgnoreCase(formatted) || l.data.name().equalsIgnoreCase(formatted)) {
                return l;
            }
        }
        return null;
    }

}

package com.questoftherealm.map;

import java.util.concurrent.ThreadLocalRandom;

public enum Locations {
    ABANDONED_TOWER("Abandoned Tower", "A crumbling tower that once served as a lookout, now eerily silent."),
    FORGOTTEN_RUINS("Forgotten Ruins", "The remains of a lost civilization, swallowed by time."),
    CRUMBLING_WATCHTOWER("Crumbling Watchtower", "Once a proud guard post, now reduced to rubble."),
    MAGES_TOWER("Mage’s Tower", "A spire where a sorcerer once dwelled, filled with lingering magic."),
    IRON_MINE("Iron Mine", "A mine carved into the earth, rich in ore but plagued with danger."),
    SHADOW_CAVERN("Shadow Cavern", "A yawning cave said to shelter creatures of the dark."),
    SACRED_GROVE("Sacred Grove", "An ancient place, protected by nature’s magic."),
    CRYSTAL_LAKE("Crystal Lake", "Shimmering waters that seem to hum with magical energy."),
    BANDIT_CAMP("Bandit Camp", "A rough encampment of outlaws and thieves."),
    SUNKEN_SWAMP("Sunken Swamp", "Rotting wetlands where the unwary often vanish."),
    ANCIENT_ALTAR("Ancient Altar", "A stone altar covered in strange markings, radiating mystery."),
    ABANDONED_HUT("Abandoned Hut", "A decrepit shack, its walls sagging with secrets long forgotten.");

    //add Goblin Locations for the quest

    private final String name;
    private final String description;

    Locations(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public static Locations generateLocation(TileTypes type) {
        return switch (type) {
            case GRASS -> randomOf(
                    Locations.SACRED_GROVE,
                    Locations.ABANDONED_HUT,
                    Locations.ANCIENT_ALTAR
            );
            case FOREST -> randomOf(
                    Locations.FORGOTTEN_RUINS,
                    Locations.CRUMBLING_WATCHTOWER,
                    Locations.ABANDONED_TOWER
            );
            case SWAMP -> randomOf(
                    Locations.SUNKEN_SWAMP,
                    Locations.SHADOW_CAVERN,
                    Locations.BANDIT_CAMP
            );
            case VILLAGE -> null;
            case CASTLE -> null;
            case MOUNTAIN -> randomOf(
                    Locations.IRON_MINE,
                    Locations.ABANDONED_TOWER,
                    Locations.CRUMBLING_WATCHTOWER
            );
            case WATER -> randomOf(
                    Locations.CRYSTAL_LAKE
            );
        };
    }


    private static Locations randomOf(Locations... options) {
        return options[ThreadLocalRandom.current().nextInt(options.length)];//returns random location
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}

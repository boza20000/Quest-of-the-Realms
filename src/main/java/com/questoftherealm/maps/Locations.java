package com.questoftherealm.maps;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public enum Locations {

    MISTY_GLADE("Misty Glade", "A foggy clearing in the forest."),
    FOREST_ENTRANCE("Forest Entrance", "The edge of an ancient woodland."),
    WHISPERING_WOODS("Whispering Woods", "Trees said to murmur secrets."),
    SUNKEN_SWAMP("Sunken Swamp", "Dangerous wetlands, hard to traverse."),
    CRYSTAL_LAKE("Crystal Lake", "Shimmering waters with magical properties."),
    FORGOTTEN_RUINS("Forgotten Ruins", "The remains of a lost civilization."),
    BANDIT_CAMP("Bandit Camp", "A hideout of thieves and outlaws."),
    KINGS_CASTLE("King’s Castle", "The seat of royal power, heavily guarded."),
    SEASIDE_VILLAGE("Seaside Village", "A fishing village by the coast."),
    PLAINS_VILLAGE("Plains Village", "A village located near a castle."),
    MOUNTAIN_VILLAGE("Mountain Village", "A village located on a cliff."),
    ABANDONED_HUT("Abandoned Hut", "A creepy hut that may hide secrets."),
    MAGES_TOWER("Mage’s Tower", "A tall spire where a sorcerer dwells."),
    WATCHTOWER("Crumbling Watchtower", "Once a guard post, now in ruins."),
    IRON_MINE("Iron Mine", "A source of wealth, but also danger."),
    SACRED_GROVE("Sacred Grove", "A magical grove protected by druids."),
    SHADOW_CAVERN("Shadow Cavern", "A dark cave full of monsters."),
    TRADING_OUTPOST("Trading Outpost","An important crossroad where traders meet");
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
                    Locations.MISTY_GLADE,
                    Locations.SACRED_GROVE,
                    Locations.ABANDONED_HUT
            );
            case FOREST -> randomOf(
                    Locations.WHISPERING_WOODS,
                    Locations.FORGOTTEN_RUINS,
                    Locations.WATCHTOWER
            );
            case SWAMP -> randomOf(
                    Locations.SUNKEN_SWAMP,
                    Locations.SHADOW_CAVERN
            );
            case VILLAGE -> randomOf(
                    Locations.SEASIDE_VILLAGE,
                    Locations.PLAINS_VILLAGE,
                    Locations.MOUNTAIN_VILLAGE
            );
            case CASTLE -> randomOf(
                    Locations.KINGS_CASTLE,
                    Locations.MAGES_TOWER
            );
            case MOUNTAIN -> randomOf(
                    Locations.MOUNTAIN_VILLAGE,
                    Locations.IRON_MINE
            );
            case WATER -> randomOf(
                    Locations.CRYSTAL_LAKE
            );
//            case QUEST_LOCATION -> randomOf(
//                    Locations.FORGOTTEN_RUINS,
//                    Locations.SHADOW_CAVERN,
//                    Locations.MAGES_TOWER
//            );
        };
    }

    private static Locations randomOf(Locations ...options) {
        return options[ThreadLocalRandom.current().nextInt(options.length)];//returns random location
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}

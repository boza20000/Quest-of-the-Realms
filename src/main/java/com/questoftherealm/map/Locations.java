package com.questoftherealm.map;

import com.questoftherealm.game.GameState;

import java.util.concurrent.ThreadLocalRandom;

public enum Locations {
    ABANDONED_TOWER(new LocationData(
            "location.abandonedTower.name",
            "location.abandonedTower.desc"
    )),
    FORGOTTEN_RUINS(new LocationData(
            "location.forgottenRuins.name",
            "location.forgottenRuins.desc"
    )),
    CRUMBLING_WATCHTOWER(new LocationData(
            "location.crumblingWatchtower.name",
            "location.crumblingWatchtower.desc"
    )),
    MAGES_TOWER(new LocationData(
            "location.magesTower.name",
            "location.magesTower.desc"
    )),
    IRON_MINE(new LocationData(
            "location.ironMine.name",
            "location.ironMine.desc"
    )),
    SHADOW_CAVERN(new LocationData(
            "location.shadowCavern.name",
            "location.shadowCavern.desc"
    )),
    SACRED_GROVE(new LocationData(
            "location.sacredGrove.name",
            "location.sacredGrove.desc"
    )),
    CRYSTAL_LAKE(new LocationData(
            "location.crystalLake.name",
            "location.crystalLake.desc"
    )),
    BANDIT_CAMP(new LocationData(
            "location.banditCamp.name",
            "location.banditCamp.desc"
    )),
    SUNKEN_SWAMP(new LocationData(
            "location.sunkenSwamp.name",
            "location.sunkenSwamp.desc"
    )),
    ANCIENT_ALTAR(new LocationData(
            "location.ancientAltar.name",
            "location.ancientAltar.desc"
    )),
    ABANDONED_HUT(new LocationData(
            "location.abandonedHut.name",
            "location.abandonedHut.desc"
    ));

    private final LocationData data;

    Locations(final LocationData data) {
        this.data = data;
    }

    public String getName() {
        return data.name();
    }

    public String getDescription() {
        return data.description();
    }

    public static Locations generateLocation(TileTypes type) {
        return switch (type) {
            case GRASS -> randomOf(SACRED_GROVE, ABANDONED_HUT, ANCIENT_ALTAR);
            case FOREST -> randomOf(FORGOTTEN_RUINS, CRUMBLING_WATCHTOWER, ABANDONED_TOWER);
            case SWAMP -> randomOf(SUNKEN_SWAMP, SHADOW_CAVERN, BANDIT_CAMP);
            case VILLAGE, CASTLE -> null;
            case MOUNTAIN -> randomOf(IRON_MINE, ABANDONED_TOWER, CRUMBLING_WATCHTOWER);
            case WATER -> randomOf(CRYSTAL_LAKE);
        };
    }

    private static Locations randomOf(Locations... options) {
        return options[ThreadLocalRandom.current().nextInt(options.length)];
    }

    public static Locations getStructure(String structure) {
        if (structure == null || structure.isBlank()) {
            return null;
        }
        String formatted = String.join("_",structure.toUpperCase().split("\\s+"));

        for (Locations l : Locations.values()) {
            if (l.name().equalsIgnoreCase(formatted) || l.data.name().equalsIgnoreCase(formatted)) {
                return l;
            }
        }
        return null;
    }
}

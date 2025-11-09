package com.questoftherealm.map;

import com.questoftherealm.localization.MessageBundle;
import java.util.concurrent.ThreadLocalRandom;

public enum Locations {

    ABANDONED_TOWER(new LocationData(
            MessageBundle.get("location.abandonedTower.name"),
            MessageBundle.get("location.abandonedTower.desc")
    )),
    FORGOTTEN_RUINS(new LocationData(
            MessageBundle.get("location.forgottenRuins.name"),
            MessageBundle.get("location.forgottenRuins.desc")
    )),
    CRUMBLING_WATCHTOWER(new LocationData(
            MessageBundle.get("location.crumblingWatchtower.name"),
            MessageBundle.get("location.crumblingWatchtower.desc")
    )),
    MAGES_TOWER(new LocationData(
            MessageBundle.get("location.magesTower.name"),
            MessageBundle.get("location.magesTower.desc")
    )),
    IRON_MINE(new LocationData(
            MessageBundle.get("location.ironMine.name"),
            MessageBundle.get("location.ironMine.desc")
    )),
    SHADOW_CAVERN(new LocationData(
            MessageBundle.get("location.shadowCavern.name"),
            MessageBundle.get("location.shadowCavern.desc")
    )),
    SACRED_GROVE(new LocationData(
            MessageBundle.get("location.sacredGrove.name"),
            MessageBundle.get("location.sacredGrove.desc")
    )),
    CRYSTAL_LAKE(new LocationData(
            MessageBundle.get("location.crystalLake.name"),
            MessageBundle.get("location.crystalLake.desc")
    )),
    BANDIT_CAMP(new LocationData(
            MessageBundle.get("location.banditCamp.name"),
            MessageBundle.get("location.banditCamp.desc")
    )),
    SUNKEN_SWAMP(new LocationData(
            MessageBundle.get("location.sunkenSwamp.name"),
            MessageBundle.get("location.sunkenSwamp.desc")
    )),
    ANCIENT_ALTAR(new LocationData(
            MessageBundle.get("location.ancientAltar.name"),
            MessageBundle.get("location.ancientAltar.desc")
    )),
    ABANDONED_HUT(new LocationData(
            MessageBundle.get("location.abandonedHut.name"),
            MessageBundle.get("location.abandonedHut.desc")
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
        String formatted = structure.trim().toLowerCase();

        for (Locations l : Locations.values()) {
            if (l.name().equalsIgnoreCase(formatted) || l.data.name().equalsIgnoreCase(formatted)) {
                return l;
            }
        }
        return null;
    }
}

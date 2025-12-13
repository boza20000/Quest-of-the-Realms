package com.questoftherealm.expeditions.missions;

import com.questoftherealm.expeditions.missions.interfaces.MissionCondition;

import com.questoftherealm.expeditions.quest.quests.*;
import com.questoftherealm.game.GameState;
import com.questoftherealm.items.Item;
import com.questoftherealm.items.ItemType;
import com.questoftherealm.game.GameConstants;

import java.util.Map;

public enum Missions {

    MEET_ELDER((p, m) -> p.getCurQuest() instanceof StartQuest q && q.isElderHasTalked()) {

    },

    GATHER_SUPPLIES((p, m) ->
    {
        boolean hasPotion = false;
        int foodSum = 0;
        if (p == null || p.getInventory() == null) {
            return false;
        }
        Map<Item, Integer> inventory = p.getInventory().getItems();
        for (Item i : inventory.keySet()) {
            int quantity = inventory.get(i);
            if (i.getType() == ItemType.POTION && quantity >= 1) {
                hasPotion = true;
            }
            if (i.getType() == ItemType.CONSUMABLES && quantity >= 1) {
                foodSum += quantity;
            }
        }
        return (p.getCurQuest() instanceof StartQuest && hasPotion && foodSum >= 5);
    }),

    TRAVEL_NORTH((p, m) -> (p.getCurQuest() instanceof NorthExploration) && p.getY() <= GameConstants.North_Y),

    EXPLORE_FORESTS((p, m) -> p.getCurQuest() instanceof GoblinAmbush && p.getPosition().equals(GameConstants.Goblin_Camp)),

    INVESTIGATE_VILLAGES((p, m) -> {
        if (!(p.getCurQuest() instanceof NorthExploration q)) return false;
        return q.isSearchedVillage1() && q.isSearchedVillage2()
                && q.isTalkedToVillager1() && q.isTalkedToVillager2();
    }),

    INFILTRATE_CAMP((p, m) -> p.getCurQuest() instanceof GoblinAmbush q && p.getPosition().equals(GameConstants.Goblin_Camp) && q.isCampFound()),

    AMBUSHED((p, m) -> (p.getCurQuest() instanceof GoblinAmbush q && p.getPosition().equals(GameConstants.Goblin_Camp) && q.isPlayerAmbushed())),

    ESCAPE_TO_SAFETY((p, m) -> p.getCurQuest() instanceof GoblinAmbush q && q.isPlayerEscapedAmbush()),

    ASSEMBLE_ARMY((p, m) -> {
        if (p.getCurQuest() instanceof RiseOfTheGoblinThreat q && q.isKnightsTriedToRecruit() && q.isArchersTriedToRecruit()
                && q.isMagesTriedToRecruit() && q.isReportedToKing()) {
            q.updateArmyStatus();
            return true;
        }
        return false;
    }),

    WARN_CASTLE((p, m) -> p.getCurQuest() instanceof RiseOfTheGoblinThreat q && p.getPosition().equals(GameConstants.Castle) && q.isReportedToKing()),

    DEFEAT_GENERAL((p, m) -> p.getCurQuest() instanceof RiseOfTheGoblinThreat q && q.isDefeated()),

    MARCH_FAR_NORTH((p, m) -> p.getCurQuest() instanceof FinalBattle && p.getPosition().equals(GameConstants.FarNorthMountain)),

    BREACH_STRONGHOLD((p, m) -> p.getCurQuest() instanceof FinalBattle q && q.isBreached()),

    DEFEAT_KING((p, m) -> (p.getCurQuest() instanceof FinalBattle q) && q.isDefeated()),

    CUSTOM_CHECK_COMPLETION(null),

    NONE(null);

    private final String nameKey;
    private final String descKey;
    private final MissionCondition condition;

    Missions(MissionCondition condition) {
        this.condition = condition;
        String base = this.name().toLowerCase();
        this.nameKey = "mission." + base + ".name";
        this.descKey = "mission." + base + ".task";
    }

    public MissionCondition getCondition() {
        return condition;
    }

    public String getNameKey(GameState state) {
        return state.getMessages().getBundle().get(nameKey);
    }

    public String getDescriptionKey(GameState state) {
        return state.getMessages().getBundle().get(descKey);
    }
}

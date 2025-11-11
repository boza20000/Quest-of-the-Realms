package com.questoftherealm.expeditions;

import com.questoftherealm.expeditions.interfaces.MissionCondition;

import com.questoftherealm.expeditions.quests.*;
import com.questoftherealm.items.Item;
import com.questoftherealm.items.ItemType;
import com.questoftherealm.game.GameConstants;
import java.util.Map;

public enum MissionConditionType {

    MEET_ELDER {
        @Override
        public MissionCondition getCondition() {
            // Logic from Meet_the_Elder.java
            return (p, m) -> p.getCurQuest() instanceof StartQuest q && q.isElderHasTalked();
        }
    },
    GATHER_SUPPLIES {
        @Override
        public MissionCondition getCondition() {
            // Logic from Gather_Supplies.java
            return (p, m) -> {
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
            };
        }
    },

    TRAVEL_NORTH {
        @Override
        public MissionCondition getCondition() {
            // Logic from Travel_North.java
            return (p, m) -> (p.getCurQuest() instanceof NorthExploration) && p.getY() <= GameConstants.North_Y;
        }
    },
    INFILTRATE_CAMP {
        @Override
        public MissionCondition getCondition() {
            // Logic from Infiltrate_the_Camp.java
            return (p, m) -> p.getCurQuest() instanceof GoblinAmbush q && p.getPosition().equals(GameConstants.Goblin_Camp) && q.isCampFound();
        }
    },
    AMBUSHED {
        @Override
        public MissionCondition getCondition() {
            // Logic from Ambushed.java
            return (p, m) -> (p.getCurQuest() instanceof GoblinAmbush q && q.isPlayerAmbushed());
        }
    },
    ESCAPE_TO_SAFETY {
        @Override
        public MissionCondition getCondition() {
            // Logic from Escape_to_Safety.java
            return (p, m) -> p.getCurQuest() instanceof GoblinAmbush q && q.isPlayerEscapedAmbush();
        }
    },
    EXPLORE_FORESTS {
        @Override
        public MissionCondition getCondition() {
            // Logic from Explore_Nearby_Forests.java
            return (p, m) -> p.getCurQuest() instanceof GoblinAmbush && p.getPosition().equals(GameConstants.Goblin_Camp);
        }
    },

    // --- RISE OF THE GOBLIN THREAT CONDITIONS ---
    WARN_CASTLE {
        @Override
        public MissionCondition getCondition() {
            // Logic from Warn_the_Castle.java
            return (p, m) -> p.getCurQuest() instanceof RiseOfTheGoblinThreat q
                    && p.getPosition().equals(GameConstants.Castle)
                    && q.isReportedToKing();
        }
    },
    DEFEAT_GENERAL {
        @Override
        public MissionCondition getCondition() {
            // Logic from Defeat_the_Goblin_General.java
            return (p, m) -> p.getCurQuest() instanceof RiseOfTheGoblinThreat q && q.isDefeated();
        }
    },

    MARCH_FAR_NORTH {
        @Override
        public MissionCondition getCondition() {
            // Logic from March_Into_the_Far_North.java
            return (p, m) -> p.getCurQuest() instanceof FinalBattle && p.getPosition().equals(GameConstants.FarNorthMountain);
        }
    },
    BREACH_STRONGHOLD {
        @Override
        public MissionCondition getCondition() {
            // Logic from Breach_the_Stronghold.java
            return (p, m) -> p.getCurQuest() instanceof FinalBattle q && q.isBreached();
        }
    },
    DEFEAT_KING {
        @Override
        public MissionCondition getCondition() {
            // Logic from Defeat_the_Goblin_King.java
            return (p, m) -> (p.getCurQuest() instanceof FinalBattle q) && q.isDefeated();
        }
    },

    CUSTOM_CHECK_COMPLETION {
        @Override
        public MissionCondition getCondition() { return null; }
    },

    NONE {
        @Override
        public MissionCondition getCondition() { return null; }
    };


    public abstract MissionCondition getCondition();
}

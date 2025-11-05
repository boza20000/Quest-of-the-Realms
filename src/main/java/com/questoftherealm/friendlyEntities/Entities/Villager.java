package com.questoftherealm.friendlyEntities.Entities;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.missions.Explore_the_Village;
import com.questoftherealm.friendlyEntities.NpcType;
import com.questoftherealm.friendlyEntities.Npc;
import com.questoftherealm.game.GameConstants;
import com.questoftherealm.game.Position;
import com.questoftherealm.interaction.MissionInteractions;

public class Villager extends Npc {
    private boolean hasTalkedVillage1 = false;
    private boolean hasTalkedVillage2 = false;

    public Villager(String name) {
        super(NpcType.Villager, name);
    }

    @Override
    public void talk(Player player, boolean isSimulation) {
        Position pos = new Position(player.getX(), player.getY());

        Explore_the_Village mission = null;
        if (player.getCurMission() instanceof Explore_the_Village ev) {
            mission = ev;
        } else if (player.getCurQuest() != null) {
            for (var m : player.getCurQuest().getMissions()) {
                if (m instanceof Explore_the_Village ev) {
                    mission = ev;
                    break;
                }
            }
        }

        if (mission == null) {
            System.out.println("No relevant mission active for this conversation.");
            return;
        }

        if (pos.equals(GameConstants.NorthVillage_1)) {
            if (!mission.isSearched_1()) {
                System.out.println("You should search the village before speaking to survivors.");
                return;
            }
            if (!hasTalkedVillage1) {
                hasTalkedVillage1 = true;
                MissionInteractions.villagerDialogue(player, 1);
                System.out.println("✅ You have explored North Village 1.");
            } else {
                System.out.println("The villager looks tired. He has nothing more to say.");
            }

        } else if (pos.equals(GameConstants.NorthVillage_2)) {
            if (!mission.isSearched_2()) {
                System.out.println("You should search the village before speaking to survivors.");
                return;
            }
            if (!hasTalkedVillage2) {
                hasTalkedVillage2 = true;
                MissionInteractions.villagerDialogue(player, 2);
                System.out.println("✅ You have explored North Village 2.");
            } else {
                System.out.println("The villager avoids your gaze, saying no more.");
            }

        } else {
            System.out.println("There are no people here.");
        }
    }

    @Override
    public boolean isHasTalked() {
        return hasTalkedVillage1 && hasTalkedVillage2;
    }
}

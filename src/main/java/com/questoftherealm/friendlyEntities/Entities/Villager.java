package com.questoftherealm.friendlyEntities.Entities;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.missions.Explore_the_Village;
import com.questoftherealm.friendlyEntities.NpcType;
import com.questoftherealm.friendlyEntities.Npc;
import com.questoftherealm.game.GameConstants;
import com.questoftherealm.game.Position;
import com.questoftherealm.interaction.MissionInteractions;

import java.util.ResourceBundle;

public class Villager extends Npc {
    private boolean hasTalkedVillage1 = false;
    private boolean hasTalkedVillage2 = false;
    private static final ResourceBundle messages = ResourceBundle.getBundle("messages");

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
            System.out.println(messages.getString("villager.noMission"));
            return;
        }

        if (pos.equals(GameConstants.NorthVillage_1)) {
            if (!mission.isSearched_1()) {
                System.out.println(messages.getString("villager.searchFirst"));
                return;
            }
            if (!hasTalkedVillage1) {
                hasTalkedVillage1 = true;
                MissionInteractions.villagerDialogue(player, 1);
                System.out.println("✅ " + messages.getString("villager.explored1"));
            } else {
                System.out.println(messages.getString("villager.tired"));
            }

        } else if (pos.equals(GameConstants.NorthVillage_2)) {
            if (!mission.isSearched_2()) {
                System.out.println(messages.getString("villager.searchFirst"));
                return;
            }
            if (!hasTalkedVillage2) {
                hasTalkedVillage2 = true;
                MissionInteractions.villagerDialogue(player, 2);
                System.out.println("✅ " + messages.getString("villager.explored2"));
            } else {
                System.out.println(messages.getString("villager.avoids"));
            }

        } else {
            System.out.println(messages.getString("villager.noPeople"));
        }
    }

    @Override
    public boolean isHasTalked() {
        return hasTalkedVillage1 && hasTalkedVillage2;
    }
}

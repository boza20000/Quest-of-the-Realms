package com.questoftherealm.friendlyEntities.Entities;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.expeditions.missions.Investigate_Northern_Villages;
import com.questoftherealm.expeditions.quests.NorthExploration;
import com.questoftherealm.friendlyEntities.NpcType;
import com.questoftherealm.friendlyEntities.Npc;
import com.questoftherealm.game.*;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.interaction.MissionInteractions;
import com.questoftherealm.map.Tile;

import java.util.ResourceBundle;

public class Villager extends Npc {
    private final ResourceBundle messages = ResourceBundle.getBundle("messages");
    private Output output;
    private boolean simulation;

    public Villager(String id, GameState state, MissionInteractions missionInteractions) {
        super(NpcType.VILLAGER, id, state, missionInteractions);
        this.output = state.getGameServices().getOutput();
    }

    @Override
    public void talk(GameState state, Player player, boolean isSimulation) {
        simulation = isSimulation;
        Position pos = new Position(player.getX(), player.getY());
        Tile tile = state.getMap().curZone(pos.x(), pos.y());
        Mission mission = player.getCurMission();

        if (!(player.getCurQuest() instanceof NorthExploration q)) {
            output.println(state.getMessages().getBundle().get("villager.doesnt.know.you"));
            return;
        }

        if (mission == null) {
            output.println(messages.getString("villager.noMission"));
            return;
        }

        if (mission instanceof Investigate_Northern_Villages nv) {
            handleTalkToSurvivors(player, tile, pos, nv, q);

        } else {
            output.println(state.getMessages().getBundle().get("villager.nothing.to.say"));
        }
    }

    private void handleTalkToSurvivors(Player player, Tile tile, Position pos, Investigate_Northern_Villages curMission, NorthExploration quest) {
        if (pos.equals(GameConstants.NorthVillage_1) && checkStatusVillage1(quest, tile)) {
            northVillager1(player, quest);
        } else if (pos.equals(GameConstants.NorthVillage_2) && checkStatusVillage2(quest, tile)) {
            northVillager2(player, quest);
        } else {
            output.println(messages.getString("villager.noPeople"));
        }
    }

    private boolean checkStatusVillage1(NorthExploration quest, Tile tile) {
        if (!quest.isSearchedVillage1()) {
            output.println(messages.getString("villager.searchFirst"));
            return false;
        }
        if (!tile.getEnemies().isEmpty()) {
            output.println(messages.getString("villager.defeatEnemiesFirst"));
            return false;
        }
        return true;
    }

    private boolean checkStatusVillage2(NorthExploration quest, Tile tile) {
        if (!quest.isSearchedVillage2()) {
            output.println(messages.getString("villager.searchFirst"));
            return false;
        }
        if (!tile.getEnemies().isEmpty()) {
            output.println(messages.getString("villager.defeatEnemiesFirst"));
            return false;
        }
        return true;
    }

    private void northVillager1(Player player, NorthExploration quest) {
        if (!quest.isTalkedToVillager1()) {
            if (!simulation) {
                getMissionInteractions().villagerDialogue(player, 1);
            }
            quest.setTalkedToVillager1(true);
            output.println("✅ " + messages.getString("villager.explored1"));
        } else {
            output.println(messages.getString("villager.tired"));
        }
    }

    private void northVillager2(Player player, NorthExploration quest) {
        if (!quest.isTalkedToVillager2()) {
            if (!simulation) {
                getMissionInteractions().villagerDialogue(player, 2);
            }
            quest.setTalkedToVillager2(true);
            output.println("✅ " + messages.getString("villager.explored2"));
        } else {
            output.println(messages.getString("villager.avoids"));
        }
    }

}

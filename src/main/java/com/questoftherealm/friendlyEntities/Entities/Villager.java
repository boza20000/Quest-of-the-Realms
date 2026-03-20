package com.questoftherealm.friendlyEntities.Entities;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.missions.Mission;
import com.questoftherealm.expeditions.missions.Missions;
import com.questoftherealm.expeditions.quest.quests.NorthExploration;
import com.questoftherealm.friendlyEntities.NpcType;
import com.questoftherealm.friendlyEntities.Npc;
import com.questoftherealm.game.*;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.interaction.MissionInteractions;
import com.questoftherealm.map.Tile;

public class Villager extends Npc {
    private boolean simulation;

    public Villager(String id, GameState state, MissionInteractions missionInteractions) {
        super(NpcType.VILLAGER, id, state, missionInteractions);
    }

    @Override
    public void talk(GameState state, Player player, boolean isSimulation) {
        Output output = state.getGameServices().getOutput();
        simulation = isSimulation;
        Position pos = new Position(player.getX(), player.getY());
        Tile tile = state.getMap().curZone(pos.x(), pos.y());
        Mission mission = player.getCurMission();

        if (!(player.getCurQuest() instanceof NorthExploration q)) {
            output.println(state.getMessages().getBundle().get("villager.doesnt.know.you"));
            return;
        }

        if (mission == null) {
            output.println(state.getMessages().getBundle().get("villager.noMission"));
            return;
        }

        if (mission.getMissionType().equals(Missions.INVESTIGATE_VILLAGES)) {
            handleTalkToSurvivors(output, player, tile, pos, q, state);
        } else {
            output.println(state.getMessages().getBundle().get("villager.nothing.to.say"));
        }
    }

    private void handleTalkToSurvivors(Output output, Player player, Tile tile, Position pos, NorthExploration quest, GameState state) {
        if (pos.equals(GameConstants.NorthVillage_1) && checkStatusVillage1(output, quest, tile, state)) {
            northVillager1(output, player, quest, state);
        } else if (pos.equals(GameConstants.NorthVillage_2) && checkStatusVillage2(output, quest, tile, state)) {
            northVillager2(output, player, quest, state);
        } else {
            output.println(state.getMessages().getBundle().get("villager.noPeople"));
        }
    }

    private boolean checkStatusVillage1(Output output, NorthExploration quest, Tile tile, GameState state) {
        if (!quest.isSearchedVillage1()) {
            output.println(state.getMessages().getBundle().get("villager.searchFirst"));
            return false;
        }
        if (!tile.getEnemies().isEmpty()) {
            output.println(state.getMessages().getBundle().get("villager.defeatEnemiesFirst"));
            return false;
        }
        return true;
    }

    private boolean checkStatusVillage2(Output output, NorthExploration quest, Tile tile, GameState state) {
        if (!quest.isSearchedVillage2()) {
            output.println(state.getMessages().getBundle().get("villager.searchFirst"));
            return false;
        }
        if (!tile.getEnemies().isEmpty()) {
            output.println(state.getMessages().getBundle().get("villager.defeatEnemiesFirst"));
            return false;
        }
        return true;
    }

    private void northVillager1(Output output, Player player, NorthExploration quest, GameState state) {
        if (!quest.isTalkedToVillager1()) {
            if (!simulation) {
                getMissionInteractions().villagerDialogue(player, 1);
            }
            quest.setTalkedToVillager1(true);
            output.println("✅ " + state.getMessages().getBundle().get("villager.explored1"));
        } else {
            output.println(state.getMessages().getBundle().get("villager.tired"));
        }
    }

    private void northVillager2(Output output, Player player, NorthExploration quest, GameState state) {
        if (!quest.isTalkedToVillager2()) {
            if (!simulation) {
                getMissionInteractions().villagerDialogue(player, 2);
            }
            quest.setTalkedToVillager2(true);
            output.println("✅ " + state.getMessages().getBundle().get("villager.explored2"));
        } else {
            output.println(state.getMessages().getBundle().get("villager.avoids"));
        }
    }
}

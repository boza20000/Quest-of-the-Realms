package com.questoftherealm.friendlyEntities.Entities;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.expeditions.missions.Investigate_Northern_Villages;
import com.questoftherealm.expeditions.quests.NorthExploration;
import com.questoftherealm.friendlyEntities.NpcType;
import com.questoftherealm.friendlyEntities.Npc;
import com.questoftherealm.game.Game;
import com.questoftherealm.game.GameConstants;
import com.questoftherealm.game.Position;
import com.questoftherealm.interaction.MissionInteractions;
import com.questoftherealm.map.Tile;

import java.util.ResourceBundle;

public class Villager extends Npc {
    private static final ResourceBundle messages = ResourceBundle.getBundle("messages");

    public Villager(String id) {
        super(NpcType.VILLAGER, id);
    }

    @Override
    public void talk(Player player, boolean isSimulation) {

        Position pos = new Position(player.getX(), player.getY());
        Tile tile = Game.getGameMap().curZone(pos.x(), pos.y());
        Mission mission = player.getCurMission();

        if (!(player.getCurQuest() instanceof NorthExploration q)) {
            System.out.println("The villager doesn’t seem to know you.");
            return;
        }

        if (mission == null) {
            System.out.println(messages.getString("villager.noMission"));
            return;
        }

        if (mission instanceof Investigate_Northern_Villages nv) {
            handleTalkToSurvivors(player,tile,pos,nv,q);

        } else {
            System.out.println("The villager has nothing to say right now.");
        }
    }

    private void handleTalkToSurvivors(Player player, Tile tile, Position pos, Investigate_Northern_Villages curMission,NorthExploration quest) {
        if (pos.equals(GameConstants.NorthVillage_1) && checkStatusVillage1(quest,tile)) {
            northVillager1(player, quest);
        } else if (pos.equals(GameConstants.NorthVillage_2) && checkStatusVillage2(quest, tile)) {
            northVillager2(player, quest);
        } else {
            System.out.println(messages.getString("villager.noPeople"));
        }
    }

    private boolean checkStatusVillage1(NorthExploration quest,Tile tile){
        if (!quest.isSearchedVillage1()) {
            System.out.println(messages.getString("villager.searchFirst"));
            return false;
        }
        if (!tile.getEnemies().isEmpty()) {
            System.out.println(messages.getString("villager.defeatEnemiesFirst"));
            return false;
        }
        return true;
    }

    private boolean checkStatusVillage2(NorthExploration quest,Tile tile){
        if (!quest.isSearchedVillage2()) {
            System.out.println(messages.getString("villager.searchFirst"));
            return false;
        }
        if (!tile.getEnemies().isEmpty()) {
            System.out.println(messages.getString("villager.defeatEnemiesFirst"));
            return false;
        }
        return true;
    }

    private void northVillager1(Player player, NorthExploration quest) {
        if (!quest.isTalkedToVillager1()) {
            MissionInteractions.villagerDialogue(player, 1);
            quest.setTalkedToVillager1(true);
            System.out.println("✅ " + messages.getString("villager.explored1"));
        } else {
            System.out.println(messages.getString("villager.tired"));
        }
    }

    private void northVillager2(Player player, NorthExploration quest) {
        if (!quest.isTalkedToVillager2()) {
            MissionInteractions.villagerDialogue(player, 2);
            quest.setTalkedToVillager2(true);
            System.out.println("✅ " + messages.getString("villager.explored2"));
        } else {
            System.out.println(messages.getString("villager.avoids"));
        }
    }

}

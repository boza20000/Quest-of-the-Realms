package com.questoftherealm.friendlyEntities.Entities;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.expeditions.missions.Explore_the_Village;
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
    private Explore_the_Village curMission = null;
    private boolean hasTalked1 = false;
    private boolean hasTalked2 = false;

    public Villager(String id) {
        super(NpcType.Villager, id);
    }

    @Override
    public void talk(Player player, boolean isSimulation) {
        Position pos = new Position(player.getX(), player.getY());
        Tile tile = Game.getGameMap().curZone(pos.x(), pos.y());

        if (player.getCurMission() instanceof Explore_the_Village ev) {
            curMission = ev;
        } else if (player.getCurQuest() != null) {
            for (var m : player.getCurQuest().getMissions()) {
                if (m instanceof Explore_the_Village ev) {
                    curMission = ev;
                    break;
                }
            }
        }
        if (curMission == null) {
            System.out.println(messages.getString("villager.noMission"));
            return;
        }

        if (pos.equals(GameConstants.NorthVillage_1)) {
            if (!curMission.isSearched_1()) {
                System.out.println(messages.getString("villager.searchFirst"));
                return;
            }
            if (!tile.getEnemies().isEmpty()) {
                System.out.println(messages.getString("villager.defeatEnemiesFirst"));
            }
            northVillager1(player, curMission);

        } else if (pos.equals(GameConstants.NorthVillage_2)) {
            if (!curMission.isSearched_2()) {
                System.out.println(messages.getString("villager.searchFirst"));
                return;
            }
            if (!tile.getEnemies().isEmpty()) {
                System.out.println(messages.getString("villager.defeatEnemiesFirst"));
            }
            northVillager2(player, curMission);
        } else {
            System.out.println(messages.getString("villager.noPeople"));
        }
    }


    private void northVillager1(Player player, Explore_the_Village mission) {
        if (!mission.isSearched_1()) {
            MissionInteractions.villagerDialogue(player, 1);
            hasTalked1 = true;
            System.out.println("✅ " + messages.getString("villager.explored1"));
        } else {
            System.out.println(messages.getString("villager.tired"));
        }
    }

    private void northVillager2(Player player, Explore_the_Village mission) {
        if (!mission.isSearched_2()) {
            MissionInteractions.villagerDialogue(player, 2);
            hasTalked2 = true;
            System.out.println("✅ " + messages.getString("villager.explored2"));
        } else {
            System.out.println(messages.getString("villager.avoids"));
        }
    }

    @Override
    public boolean isHasTalked() {
        return hasTalked1 && hasTalked2;
    }
}

package com.questoftherealm.friendlyEntities.Entities;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.missions.Explore_the_Village;
import com.questoftherealm.friendlyEntities.FriendInterfaces.Friendly;
import com.questoftherealm.friendlyEntities.Npc;
import com.questoftherealm.game.GameConstants;
import com.questoftherealm.game.Position;
import com.questoftherealm.interaction.Interactions;

public class Villager extends Npc implements Friendly {
    private static boolean hasTalkedVillage1 = false;
    private static boolean hasTalkedVillage2 = false;

    public Villager() {
    }

    @Override
    public void talk(Player player) {
        Position pos = new Position(player.getX(),player.getY());
        if (pos.equals(GameConstants.NorthVillage_1)) {
            if (!Explore_the_Village.isHasSearched_1()) {
                System.out.println("You should search the village before speaking to survivors.");
                return;
            }
            if (!hasTalkedVillage1) {
                hasTalkedVillage1 = true;
                Interactions.villagerDialogue(player, 1);
                System.out.println("✅ You have explored North Village 1.");
            } else {
                System.out.println("The villager looks tired. He has nothing more to say.");
            }
        } else if (pos.equals(GameConstants.NorthVillage_2)) {
            if (!Explore_the_Village.isHasSearched_2()) {
                System.out.println("You should search the village before speaking to survivors.");
                return;
            }
            if (!hasTalkedVillage2) {
                hasTalkedVillage2 = true;
                Interactions.villagerDialogue(player, 2);
                System.out.println("✅ You have explored North Village 2.");
            } else {
                System.out.println("The villager avoids your gaze, saying no more.");
            }
        } else {
            System.out.println("There are no people here.");
        }
    }
    public static boolean hasTalkedToAll() {
        return hasTalkedVillage1 && hasTalkedVillage2;
    }

}

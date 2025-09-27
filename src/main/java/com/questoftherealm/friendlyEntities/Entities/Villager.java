package com.questoftherealm.friendlyEntities.Entities;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.friendlyEntities.FriendInterfaces.Friendly;
import com.questoftherealm.friendlyEntities.Npc;
import com.questoftherealm.interaction.Interactions;

public class Villager extends Npc implements Friendly {
    private static boolean hasTalked;
    public Villager(){}

    @Override
    public void talk(Player player) {
        hasTalked = true;
        Interactions.villagerDialogue(player);
    }

    public static boolean isHasTalked() {
        return hasTalked;
    }

}

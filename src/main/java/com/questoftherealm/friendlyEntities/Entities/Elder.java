package com.questoftherealm.friendlyEntities.Entities;

import com.questoftherealm.friendlyEntities.FriendInterfaces.Friendly;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.friendlyEntities.Npc;
import com.questoftherealm.items.Chest;
import com.questoftherealm.items.ItemDrop;

public class Elder extends Npc implements Friendly {
    private final String name = "Evaery";
    private static boolean hasTalked = false;

    public Elder() {
    }

    public static boolean isHasTalked() {
        return hasTalked;
    }

    @Override
    public void talk(Player player) {
        if (!hasTalked) {
            hasTalked = true;
            System.out.println("The eldar " + name + " is one of the councils wisest people");
            System.out.println("He is urging you to prepare well for the expedition.");
            System.out.println("He will give you some items to help you on your journey.");
            ItemDrop weapon = Chest.generateRandomWeapon(player);
            System.out.println("A " + weapon.item().getEffect() + " to protect yourself from what you meet in the north and on the way there");
            System.out.println("This weapon should provide with the needed extra protection to reach the objective.");
            player.equipWeapon(weapon.item());
            System.out.println("You have received " + weapon.item().getName());
            System.out.println("The king also gathered his best blacksmiths to make a strong armor to help you against the eminent treat.");
            ItemDrop helmet = Chest.generateRandomHelmet(player);
            ItemDrop chestplate = Chest.generateRandomChestplate(player);
            ItemDrop boots = Chest.generateRandomBoots(player);
            System.out.println("You have received " + helmet.item().getName() + " "
                    + chestplate.item().getName() + " "
                    + boots.item().getName());
            player.equipArmorPiece(helmet.item());
            player.equipArmorPiece(chestplate.item());
            player.equipArmorPiece(boots.item());
        } else {
            System.out.println("There is nothing else to be said");
        }

    }
}

package com.questoftherealm.friendlyEntities.Entities;

import com.questoftherealm.friendlyEntities.FriendInterfaces.Friendly;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.friendlyEntities.Npc;
import com.questoftherealm.game.GameConstants;
import com.questoftherealm.interaction.Interactions;
import com.questoftherealm.interaction.SlowPrinter;
import com.questoftherealm.items.Chest;
import com.questoftherealm.items.ItemDrop;

import java.util.List;

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
            // Give weapon
            ItemDrop weapon = Chest.generateRandomWeapon(player);
            player.equipWeapon(weapon.item());
            // Give armor
            ItemDrop helmet = Chest.generateRandomHelmet(player);
            ItemDrop chestplate = Chest.generateRandomChestplate(player);
            ItemDrop boots = Chest.generateRandomBoots(player);
            player.equipArmorPiece(helmet.item());
            player.equipArmorPiece(chestplate.item());
            player.equipArmorPiece(boots.item());

            Interactions.elderDialogue(name,weapon,helmet,chestplate,boots);

        } else {
            System.out.println("There is nothing else to be said");
        }
    }
}

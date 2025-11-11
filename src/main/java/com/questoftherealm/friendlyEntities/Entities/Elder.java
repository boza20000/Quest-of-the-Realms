package com.questoftherealm.friendlyEntities.Entities;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.quests.StartQuest;
import com.questoftherealm.friendlyEntities.NpcType;
import com.questoftherealm.friendlyEntities.Npc;
import com.questoftherealm.interaction.MissionInteractions;
import com.questoftherealm.items.Chest;
import com.questoftherealm.items.ItemDrop;
import com.questoftherealm.localization.MessageBundle;

public class Elder extends Npc {
    private final String name = MessageBundle.get("elder.name");

    public Elder(String name) {
        super(NpcType.ELDER,name);
    }

    @Override
    public void talk(Player player, boolean isSimulation) {
        if (!(player.getCurQuest() instanceof StartQuest q)) {
            System.out.println(MessageBundle.get("elder.confused"));
            return;
        }
        if (!q.isElderHasTalked()) {
            q.setElderHasTalked(true);
            giveRewards(player, isSimulation);
        } else {
            System.out.println(MessageBundle.get("elder.has.talked"));
        }
    }

    private void giveRewards(Player player, boolean simulate) {
        ItemDrop weapon = Chest.generateRandomWeapon(player);
        player.getInventory().addItem(weapon.item(), weapon.quantity());
        // Give armor
        ItemDrop helmet = Chest.generateRandomHelmet(player);
        ItemDrop chestplate = Chest.generateRandomChestplate(player);
        ItemDrop boots = Chest.generateRandomBoots(player);
        player.getInventory().addItem(helmet.item(), helmet.quantity());
        player.getInventory().addItem(chestplate.item(), chestplate.quantity());
        player.getInventory().addItem(boots.item(), boots.quantity());
        if (!simulate) {
            MissionInteractions.elderDialogue(name, weapon, helmet, chestplate, boots);
        }
    }

}

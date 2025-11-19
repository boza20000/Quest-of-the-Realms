package com.questoftherealm.friendlyEntities.Entities;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.quests.StartQuest;
import com.questoftherealm.friendlyEntities.NpcType;
import com.questoftherealm.friendlyEntities.Npc;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.interaction.MissionInteractions;
import com.questoftherealm.items.Chest;
import com.questoftherealm.items.ItemDrop;
import com.questoftherealm.localization.MessageBundle;

public class Elder extends Npc {
    private final String name = MessageBundle.get("elder.name");
    private Output output;

    public Elder(String id, GameState state, MissionInteractions missionInteractions) {
        super(NpcType.ELDER, id, state, missionInteractions);
        this.output = state.getGameServices().getOutput();
    }

    @Override
    public void talk(GameState state, Player player, boolean isSimulation) {
        if (!(player.getCurQuest() instanceof StartQuest q)) {
            output.println(MessageBundle.get("elder.confused"));
            return;
        }
        if (!q.isElderHasTalked()) {
            q.setElderHasTalked(true);
            giveRewards(player, isSimulation, state);
        } else {
            output.println(MessageBundle.get("elder.has.talked"));
        }
    }

    private void giveRewards(Player player, boolean simulate, GameState state) {
        Chest chest = new Chest(state);
        ItemDrop weapon = chest.generateRandomWeapon(player);
        player.getInventory().addItem(weapon.item(), weapon.quantity(), state);
        // Give armor
        ItemDrop helmet = chest.generateRandomHelmet(player);
        ItemDrop chestplate = chest.generateRandomChestplate(player);
        ItemDrop boots = chest.generateRandomBoots(player);
        player.getInventory().addItem(helmet.item(), helmet.quantity(), state);
        player.getInventory().addItem(chestplate.item(), chestplate.quantity(), state);
        player.getInventory().addItem(boots.item(), boots.quantity(), state);
        if (!simulate) {
            getMissionInteractions().elderDialogue(name, weapon, helmet, chestplate, boots);
        }
    }

}

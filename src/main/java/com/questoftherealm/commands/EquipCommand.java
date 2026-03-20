package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Inventory;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.exceptions.ItemNotFound;
import com.questoftherealm.game.GameState;
import com.questoftherealm.items.Item;
import com.questoftherealm.server.ServerLogger;

import java.util.Arrays;

public class EquipCommand extends Command {
    public EquipCommand() {
        super("equip");
    }

    @Override
    public String getDescription(GameState state) {
        return state.getMessages().getBundle().get("equip.description");
    }

    @Override
    public void execute(String[] args,Player player, GameState state) {
        if(!makeSafe(args,player,state)){
            return;
        }
        String itemName = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        try {
            Item item = state.getItemRegistry().getItem(itemName);
            Inventory inventory = player.getInventory();
            synchronized (inventory) {
                if (!inventory.containsItem(item)) {
                    state.getGameServices().getOutput().println(state.getMessages().getBundle().get("equip.error.notInInventory", itemName));
                    return;
                }
                player.equipItem(item, state);
                inventory.removeItem(item, 1, state);
            }
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("equip.success", item.getName()));
        } catch (ItemNotFound e) {
            ServerLogger.get().info("EquipCommand: Item not found for player " + player.getName());
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("equip.error.itemDoesNotExist"));
        } catch (IllegalArgumentException e) {
            ServerLogger.get().warn("EquipCommand: Unexpected error equipping item for player " + player.getName(), e);
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("equip.error.unexpected"));
        }

    }

    @Override
    public boolean makeSafe(String[] args, Player player,GameState state) {
        if (args.length < 2) {
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("equip.usage", getDescription(state)));
            return false;
        }
        return playerBaseCheck(player,state);
    }
}
package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Inventory;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.exceptions.ItemNotFound;
import com.questoftherealm.game.GameState;
import com.questoftherealm.items.Item;
import java.util.Arrays;
import static com.questoftherealm.items.ItemRegistry.getItem;

public class EquipCommand extends Command {
    public EquipCommand() {
        super("equip");
    }

    @Override
    public String getDescription() {
        return "equip [item name] — equips an item from your inventory if available";
    }

    @Override
    public void execute(String[] args,Player player, GameState state) {
        if(!makeSafe(args,player,state)){
            return;
        }
        String itemName = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        try {
            Item item = getItem(itemName);
            Inventory inventory = player.getInventory();
            if (!inventory.containsItem(item)) {
                state.getGameServices().getOutput().println("You don't have '" + itemName + "' in your inventory.");
                return;
            }
            player.equipItem(item,state);
            state.getGameServices().getOutput().println(item.getName() + " has been equipped successfully!");
        } catch (ItemNotFound e) {
            state.getGameServices().getOutput().println("This item doesn't exist.");
        } catch (Exception e) {
            state.getGameServices().getOutput().println("An unexpected error occurred while equipping the item.");
        }

    }

    @Override
    public boolean makeSafe(String[] args, Player player,GameState state) {
        if (args.length < 2) {
            state.getGameServices().getOutput().println("Usage: " + getDescription());
            return false;
        }
        return playerBaseCheck(player,state);
    }
}

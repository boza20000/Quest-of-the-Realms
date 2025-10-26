package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Inventory;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.exceptions.ItemNotFound;
import com.questoftherealm.game.Game;
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
    public void execute(String[] args) {
        Player player = Game.getPlayer();
        if(!makeSafe(args,player)){
            return;
        }
        String itemName = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        try {
            Item item = getItem(itemName);
            Inventory inventory = player.getInventory();
            if (!inventory.containsItem(item)) {
                System.out.println("You don't have '" + itemName + "' in your inventory.");
                return;
            }
            player.equipItem(item);
            System.out.println(item.getName() + " has been equipped successfully!");
        } catch (ItemNotFound e) {
            System.out.println("This item doesn't exist.");
        } catch (Exception e) {
            System.out.println("An unexpected error occurred while equipping the item.");
        }

    }

    @Override
    public boolean makeSafe(String[] args, Player player) {
        if (args.length < 2) {
            System.out.println("Usage: " + getDescription());
            return false;
        }
        return playerBaseCheck(player);
    }
}

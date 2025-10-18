package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Inventory;
import com.questoftherealm.exceptions.ItemNotFound;
import com.questoftherealm.game.Game;
import com.questoftherealm.items.Item;

import static com.questoftherealm.items.ItemRegistry.getItem;

public class EquipCommand extends Command {
    public EquipCommand() {
        super("equip");
    }

    @Override
    public String getDescription() {
        return "equip [itemName]";
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: equip [itemName]");
            return;
        }
        if (Game.getPlayer() == null) {
            System.out.println("Error: No player currently active.");
            return;
        }

        String itemName = args[1];
        try {
            Item item = getItem(itemName);
            Inventory inventory = Game.getPlayer().getInventory();
            if (!inventory.containsItem(item)) {
                System.out.println("You don't have '" + itemName + "' in your inventory.");
                return;
            }
            Game.getPlayer().equipItem(item);
            System.out.println(item.getName() + " has been equipped successfully!");
        } catch (ItemNotFound e) {
            System.out.println("This item doesn't exist.");
        } catch (Exception e) {
            System.out.println("An unexpected error occurred while equipping the item.");
            e.printStackTrace(System.err);
        }

    }
}

package com.questoftherealm.commands;

import com.questoftherealm.exceptions.ItemNotFound;
import com.questoftherealm.game.Game;
import com.questoftherealm.items.Item;

import static com.questoftherealm.items.ItemRegistry.getItem;


public class UseCommand extends Command {

    public UseCommand() {
        super("use");
    }

    @Override
    public String getDescription() {
        return "use [itemName]";
    }

    @Override
    public void execute(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: " + getDescription());
            return;
        }
        if (Game.getPlayer() == null) {
            System.out.println("Error: No player currently active.");
            return;
        }
        String nameItem = args[1];
        Item item;
        try {
            item = getItem(nameItem);
        } catch (IllegalArgumentException e) {
            System.out.println("This is not item");
            return;
        } catch (ItemNotFound ex) {
            System.out.println("Item not found");
            return;
        }
        if (Game.getPlayer().getInventory().containsItem(item)) {
            Game.getPlayer().useItem(item);
            Game.getPlayer().getInventory().removeItem(item, 1);
        }
    }
}

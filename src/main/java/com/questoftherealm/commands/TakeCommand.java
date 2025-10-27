package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.game.Game;
import com.questoftherealm.items.Item;
import com.questoftherealm.items.ItemDrop;
import com.questoftherealm.map.Map;
import com.questoftherealm.map.Tile;

import static com.questoftherealm.items.ItemRegistry.getItem;

public class TakeCommand extends Command {
    public TakeCommand() {
        super("take");
    }

    @Override
    public boolean makeSafe(String[] args, Player player) {
        if (args.length < 2) {
            System.out.println("Usage: " + getDescription());
            return false;
        }
        return playerBaseCheck(player);
    }

    @Override
    public void execute(String[] args) {
        Player player = Game.getPlayer();
        if(!makeSafe(args, player)){
            return;
        }

        String input = String.join(" ", args);
        String command = args[0];
        String rest = input.substring(command.length()).trim();
        int lastSpace = rest.lastIndexOf(" ");
        if (lastSpace == -1) {
            System.out.println("You must specify a quantity.");
            return;
        }
        String itemName = rest.substring(0, lastSpace).trim();
        int quantity;
        try {
            quantity = Integer.parseInt(rest.substring(lastSpace + 1));
        } catch (NumberFormatException e) {
            System.out.println("Quantity must be a number.");
            return;
        }
        Item newItem;
        try {
            newItem = getItem(itemName);
        } catch (Exception e) {
            System.out.println("Item unknown");
            return;
        }
        Map map = Game.getGameMap();
        Tile curZone = map.curZone(player.getX(), player.getY());

        if (Game.getGameMap() == null) {
            System.out.println("Map was not loaded");
            return;
        }
        if (curZone == null) {
            System.out.println("unknown zone");
            return;
        }

        ItemDrop drop = curZone.getDrops().stream()
                .filter(d -> d.item().equals(newItem))
                .findFirst()
                .orElse(null);

        if (drop != null && drop.quantity() >= quantity) {
            player.getInventory().addItem(newItem, quantity);
            curZone.removeDrop(newItem, quantity);
            System.out.println("You picked up " + quantity + "x " + newItem.getName());
        } else {
            System.out.println("No such item or not enough quantity.");
        }
    }

    @Override
    public String getDescription() {
        return "take [item name] [quantity] — picks up the specified number of an item from the ground";
    }
}

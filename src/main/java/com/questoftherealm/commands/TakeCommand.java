package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.game.GameState;
import com.questoftherealm.items.Item;
import com.questoftherealm.items.ItemDrop;
import com.questoftherealm.items.ItemRegistry;
import com.questoftherealm.map.Map;
import com.questoftherealm.map.Tile;


public class TakeCommand extends Command {
    public TakeCommand() {
        super("take");
    }

    @Override
    public boolean makeSafe(String[] args, Player player, GameState state) {
        if (args.length < 2) {
            state.getGameServices().getOutput().println("Usage: " + getDescription(state));
            return false;
        }
        return playerBaseCheck(player,state);
    }

    @Override
    public void execute(String[] args, Player player, GameState state) {
        if (!makeSafe(args, player, state)) {
            return;
        }

        String input = String.join(" ", args);
        String command = args[0];
        String rest = input.substring(command.length()).trim();
        int lastSpace = rest.lastIndexOf(" ");
        if (lastSpace == -1) {
            state.getGameServices().getOutput().println("You must specify a quantity.");
            return;
        }
        String itemName = rest.substring(0, lastSpace).trim();
        int quantity;
        try {
            quantity = Integer.parseInt(rest.substring(lastSpace + 1));
        } catch (NumberFormatException e) {
            state.getGameServices().getOutput().println("Quantity must be a number.");
            return;
        }
        Item newItem;
        try {
            newItem = state.getItemRegistry().getItem(itemName);
        } catch (Exception e) {
            state.getGameServices().getOutput().println("Item unknown");
            return;
        }
        Map map = state.getMap();
        if (map == null) {
            state.getGameServices().getOutput().println("Map was not loaded");
            return;
        }
        Tile curZone = map.curZone(player.getX(), player.getY());
        if (curZone == null) {
            state.getGameServices().getOutput().println("unknown zone");
            return;
        }

        ItemDrop drop = curZone.getDrops().stream()
                .filter(d -> d.item().equals(newItem))
                .findFirst()
                .orElse(null);

        if (drop != null && drop.quantity() >= quantity) {
            player.getInventory().addItem(newItem, quantity,state);
            curZone.removeDrop(newItem, quantity,state);
            state.getGameServices().getOutput().println("You picked up " + quantity + "x " + newItem.getName());
        } else {
            state.getGameServices().getOutput().println("No such item or not enough quantity.");
        }
    }

    @Override
    public String getDescription(GameState state) {
        return "take [item name] [quantity] — picks up the specified number of an item from the ground";
    }
}

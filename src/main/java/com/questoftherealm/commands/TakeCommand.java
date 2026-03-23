package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.game.GameState;
import com.questoftherealm.exceptions.ItemNotFound;
import com.questoftherealm.items.Item;
import com.questoftherealm.map.WorldMap;
import com.questoftherealm.map.Tile;
import com.questoftherealm.server.ServerLogger;



public class TakeCommand extends Command {
    public TakeCommand() {
        super("take");
    }

    @Override
    public boolean makeSafe(String[] args, Player player, GameState state) {
        if (args.length < 2) {
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("take.usage", getDescription(state)));
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
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("take.error.specifyQuantity"));
            return;
        }
        String itemName = rest.substring(0, lastSpace).trim();
        int quantity;
        try {
            quantity = Integer.parseInt(rest.substring(lastSpace + 1));
        } catch (NumberFormatException e) {
            ServerLogger.get().warn("TakeCommand: Invalid quantity input - " + rest.substring(lastSpace + 1), e);
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("take.error.quantityNotNumber"));
            return;
        }
        Item newItem;
        try {
            newItem = state.getItemRegistry().getItem(itemName);
        } catch (ItemNotFound | IllegalArgumentException e) {
            ServerLogger.get().info("TakeCommand: Item not found or invalid - " + itemName);
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("take.error.itemUnknown"));
            return;
        }
        WorldMap map = state.getMap();
        if (map == null) {
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("take.error.mapNotLoaded"));
            return;
        }
        Tile curZone = map.curZone(player.getX(), player.getY());
        if (curZone == null) {
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("take.error.unknownZone"));
            return;
        }

        // Use synchronized takeFromTile which checks availability and removes atomically
        boolean taken = curZone.takeItem(newItem, quantity);
        if (taken) {
            player.getInventory().addItem(newItem, quantity, state);
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("take.success", quantity, newItem.getName()));
        } else {
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("take.error.noItemOrQuantity"));
        }
    }

    @Override
    public String getDescription(GameState state) {
        return state.getMessages().getBundle().get("take.description");
    }
}
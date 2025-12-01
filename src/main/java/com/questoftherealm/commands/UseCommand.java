package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.exceptions.ItemNotFound;
import com.questoftherealm.game.GameState;
import com.questoftherealm.items.Item;
import com.questoftherealm.items.ItemRegistry;

import java.util.Arrays;


public class UseCommand extends Command {

    public UseCommand() {
        super("use");
    }

    @Override
    public String getDescription(GameState state) {
        return state.getMessages().getBundle().get("use.description");
    }

    @Override
    public boolean makeSafe(String[] args, Player player, GameState state) {
        if (args.length < 2) {
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("use.usage", getDescription(state)));
            return false;
        }
        return playerBaseCheck(player,state);
    }

    @Override
    public void execute(String[] args, Player player, GameState state) {
        if (!makeSafe(args, player, state)) {
            return;
        }
        String nameItem = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        Item item;
        try {
            item = state.getItemRegistry().getItem(nameItem);
        } catch (IllegalArgumentException e) {
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("use.error.notItem"));
            return;
        } catch (ItemNotFound ex) {
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("use.error.itemNotFound"));
            return;
        }
        if (player.getInventory().containsItem(item)) {
            player.useItem(item);
            player.getInventory().removeItem(item, 1,state);
        }
    }
}
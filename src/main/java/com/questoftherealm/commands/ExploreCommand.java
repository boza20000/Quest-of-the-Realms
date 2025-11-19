package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.game.Game;
import com.questoftherealm.game.GameState;
import com.questoftherealm.map.Tile;

public class ExploreCommand extends Command {
    public ExploreCommand() {
        super("explore");
    }

    @Override
    public void execute(String[] args, Player player, GameState state) {
        if (!makeSafe(args, player,state)) {
            return;
        }
        Tile curTile = state.getMap().curZone(player.getX(), player.getY());
        if (curTile == null) {
            state.getGameServices().getOutput().println("You are in an undefined area.");
            return;
        }
        try {
            String structure = args[1];
            if (curTile.getStructure().getName().equalsIgnoreCase(structure)) {
                player.exploreStructure(structure, state);
            } else {
                state.getGameServices().getOutput().println("Structure name mismatch");
            }
        } catch (Exception e) {
            state.getGameServices().getOutput().println("Structure unavailable");
        }
    }

    @Override
    public String getDescription() {
        return "explore [structure name] — explore a nearby structure in your current zone";
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

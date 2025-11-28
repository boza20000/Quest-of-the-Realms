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
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("explore.error.undefinedArea"));
            return;
        }
        try {
            String structure = args[1];
            if (curTile.getStructure().getName().equalsIgnoreCase(structure)) {
                player.exploreStructure(structure, state);
            } else {
                state.getGameServices().getOutput().println(state.getMessages().getBundle().get("explore.error.nameMismatch"));
            }
        } catch (Exception e) {
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("explore.error.unavailable"));
        }
    }

    @Override
    public String getDescription(GameState state) {
        return state.getMessages().getBundle().get("explore.command.description");
    }

    @Override
    public boolean makeSafe(String[] args, Player player,GameState state) {
        if (args.length < 2) {
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("explore.usage", getDescription(state)));
            return false;
        }
        return playerBaseCheck(player,state);
    }
}
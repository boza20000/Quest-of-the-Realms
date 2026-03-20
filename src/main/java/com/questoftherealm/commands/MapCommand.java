package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.exceptions.MapNotLoaded;
import com.questoftherealm.game.GameState;
import com.questoftherealm.server.ServerLogger;


public class MapCommand extends Command {

    public MapCommand() {
        super("map");
    }

    @Override
    public void execute(String[] args,Player player, GameState state) {
        if (state.getMap() == null) {
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("map.error.unavailable"));
            return;
        }
        if (!makeSafe(args, player,state)) {
            return;
        }

        try {
            state.getMap().print(state);
            state.getGameServices().getOutput().flush();
        } catch (MapNotLoaded e) {
            ServerLogger.get().warn("MapCommand: Failed to print map", e);
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("map.error.printingFailed"));
        }

    }

    @Override
    public String getDescription(GameState state)  {
        return state.getMessages().getBundle().get("map.description");
    }

    @Override
    public boolean makeSafe(String[] args, Player player,GameState state) {
        if (args.length != 1) {
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("map.usage", getDescription(state)));
            return false;
        }
        return playerBaseCheck(player,state);
    }
}
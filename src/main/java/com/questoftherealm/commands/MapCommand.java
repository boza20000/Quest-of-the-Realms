package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.game.GameState;


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
        } catch (Exception e) {
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
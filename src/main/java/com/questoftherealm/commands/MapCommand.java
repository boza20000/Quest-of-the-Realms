package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.game.Game;
import com.questoftherealm.game.GameState;
import com.questoftherealm.map.Map;

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
        state.getGameServices().getOutput().print(state.getMessages().getBundle().get("map.display.padding"));
        state.getGameServices().getOutput().println(state.getMessages().getBundle().get("map.display.header"));
        try {
            state.getMap().print(player,state);
        } catch (Exception e) {
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("map.error.printingFailed"));
        }
        state.getGameServices().getOutput().print(state.getMessages().getBundle().get("map.display.padding"));
        state.getGameServices().getOutput().println(state.getMessages().getBundle().get("map.display.footer"));
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
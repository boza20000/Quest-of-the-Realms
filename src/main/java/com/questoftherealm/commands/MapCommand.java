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
            state.getGameServices().getOutput().println("Game map unavailable");
            return;
        }
        if (!makeSafe(args, player,state)) {
            return;
        }
        state.getGameServices().getOutput().print("      ");
        state.getGameServices().getOutput().println("╔════════ MAP ══════╗");
        try {
            state.getMap().print(player,state);
        } catch (Exception e) {
            state.getGameServices().getOutput().println("Something went wrong while printing the map");
        }
        state.getGameServices().getOutput().print("      ");
        state.getGameServices().getOutput().println("╚═══════════════════╝");
    }

    @Override
    public String getDescription() {
        return "map — displays the current map layout and your position";
    }

    @Override
    public boolean makeSafe(String[] args, Player player,GameState state) {
        if (args.length != 1) {
            state.getGameServices().getOutput().println("Usage: " + getDescription());
            return false;
        }
        return playerBaseCheck(player,state);
    }
}

package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.game.Game;
import com.questoftherealm.game.GameState;

public class LookCommand extends Command {

    public LookCommand() {
        super("look");
    }

    @Override
    public String getDescription() {
        return "look — observe your surroundings for items, enemies, or structures";
    }

    @Override
    public boolean makeSafe(String[] args, Player player,GameState state) {
        if (args.length != 1) {
            state.getGameServices().getOutput().println("Usage: " + getDescription());
            return false;
        }
        return playerBaseCheck(player,state);
    }

    @Override
    public void execute(String[] args, Player player, GameState state) {
        if (!makeSafe(args, player,state)) {
            return;
        }
        try {
            state.getGameServices().getOutput().print("Looking");
            for (int i = 0; i < 3; i++) {
                Thread.sleep(1000);
                state.getGameServices().getOutput().print(".");
            }
            player.look(state);
        } catch (Exception e) {
            state.getGameServices().getOutput().println("Looking failed");
        }
    }
}

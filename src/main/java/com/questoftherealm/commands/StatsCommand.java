package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.game.GameState;

public class StatsCommand extends Command {

    protected StatsCommand() {
        super("stats");
    }

    @Override
    public boolean makeSafe(String[] args, Player player, GameState state) {
        if (args.length != 1) {
            state.getGameServices().getOutput().println("Usage: " + getDescription());
            return false;
        }
        return playerBaseCheck(player,state);
    }

    @Override
    public void execute(String[] args, Player player, GameState state) {
        if (!makeSafe(args, player, state)) {
            return;
        }
        state.getGameServices().getOutput().println("Player current stats:");
        state.getGameServices().getOutput().println(player.getPlayerCharacter().toString());
    }

    @Override
    public String getDescription() {
        return "stats — displays your character’s current stats and attributes";
    }
}

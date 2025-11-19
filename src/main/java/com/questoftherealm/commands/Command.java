package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.game.GameState;

public abstract class Command {
    private final String name;

    protected Command(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract void execute(String[] args, Player player, GameState state);

    public abstract String getDescription();

    public abstract boolean makeSafe(String[] args, Player player,GameState state);

    public boolean playerBaseCheck(Player player,GameState state) {
        if (player == null) {
            state.getGameServices().getOutput().println("Error: No player loaded.");
            return false;
        }
        return true;
    }


}

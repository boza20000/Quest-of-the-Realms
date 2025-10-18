package com.questoftherealm.commands;

import com.questoftherealm.game.Game;

public class StatsCommand extends Command {

    protected StatsCommand() {
        super("stats");
    }

    @Override
    public void execute(String[] args) {
        if (args.length > 2) {
            System.out.println("Usage: stats");
            return;
        }
        if (Game.getPlayer() == null) {
            System.out.println("Error: No player currently active.");
            return;
        }
        System.out.println("Player current stats:");
        System.out.println(Game.getPlayer().getPlayerCharacter().toString());
    }

    @Override
    public String getDescription() {
        return "";
    }
}

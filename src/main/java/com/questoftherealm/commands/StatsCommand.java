package com.questoftherealm.commands;

import com.questoftherealm.game.Game;

public class StatsCommand extends Command {

    protected StatsCommand() {
        super("stats");
    }

    @Override
    public void execute(String[] args) {
        System.out.println("Player current stats:");
        System.out.println(Game.getPlayer().getPlayerCharacter().toString());
    }

    @Override
    public String getDescription() {
        return "";
    }
}

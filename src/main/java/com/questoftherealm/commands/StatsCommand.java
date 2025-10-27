package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.game.Game;

public class StatsCommand extends Command {

    protected StatsCommand() {
        super("stats");
    }

    @Override
    public boolean makeSafe(String[] args, Player player) {
        if (args.length != 1) {
            System.out.println("Usage: " + getDescription());
            return false;
        }
        return playerBaseCheck(player);
    }

    @Override
    public void execute(String[] args) {
        Player player = Game.getPlayer();
        if(!makeSafe(args, player)){
            return;
        }
        System.out.println("Player current stats:");
        System.out.println(player.getPlayerCharacter().toString());
    }

    @Override
    public String getDescription() {
        return "stats — displays your character’s current stats and attributes";
    }
}

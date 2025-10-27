package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.game.Game;

public class LookCommand extends Command {

    public LookCommand() {
        super("look");
    }

    @Override
    public String getDescription() {
        return "look — observe your surroundings for items, enemies, or structures";
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
        if (!makeSafe(args, player)) {
            return;
        }
        try {
            System.out.print("Looking");
            for (int i = 0; i < 3; i++) {
                Thread.sleep(1000);
                System.out.print(".");
            }
            player.look();
        } catch (Exception e) {
            System.out.println("Looking failed");
        }
    }
}

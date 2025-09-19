package com.questoftherealm.game.Commands;

import com.questoftherealm.game.Game;

public class LookCommand extends Command {

    public LookCommand() {
        super("look");
    }

    @Override
    public String getDescription() {
        return "looking around for (items,materials or enemies) near the players location";
    }

    @Override
    public void execute(String[] args) {
        try {
            System.out.print("Looking");
            for (int i = 0; i < 3; i++) {
                Thread.sleep(1000);
                System.out.print(".");
            }
            Game.getPlayer().look();
        } catch (Exception e) {
            System.out.println("Looking failed");
        }
    }
}

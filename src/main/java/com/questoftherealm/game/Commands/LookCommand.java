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
        Game.getPlayer().look();
    }
}

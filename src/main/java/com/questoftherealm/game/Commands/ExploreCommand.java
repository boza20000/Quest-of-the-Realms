package com.questoftherealm.game.Commands;

import com.questoftherealm.game.Game;

public class ExploreCommand extends Command {
    public ExploreCommand() {
        super("explore");
    }

    @Override
    public void execute(String[] args) {
        String structure = args[1];
        Game.getPlayer().exploreStructure(structure);
    }

    @Override
    public String getDescription() {
        return "the player explores the structure on the map sector";
    }
}

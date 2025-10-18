package com.questoftherealm.commands;

import com.questoftherealm.game.Game;
import com.questoftherealm.map.Tile;

public class ExploreCommand extends Command {
    public ExploreCommand() {
        super("explore");
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: explore [structure name]");
            return;
        }
        if (Game.getPlayer() == null) {
            System.out.println("Error: No player currently active.");
            return;
        }
        Tile curTile = Game.getGameMap().curZone(Game.getPlayer().getX(), Game.getPlayer().getY());
        if (curTile == null) {
            System.out.println("You are in an undefined area.");
            return;
        }
        try {
            String structure = args[1];
            if (curTile.getStructure().getName().equals(structure)) {
                Game.getPlayer().exploreStructure(structure);
            } else {
                System.out.println("Structure name mismatch");
            }
        } catch (Exception e) {
            System.out.println("Structure unavailable");
        }
    }

    @Override
    public String getDescription() {
        return "the player explores the structure on the map sector";
    }
}

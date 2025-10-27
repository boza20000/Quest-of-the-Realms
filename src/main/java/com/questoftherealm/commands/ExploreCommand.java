package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.game.Game;
import com.questoftherealm.map.Tile;

public class ExploreCommand extends Command {
    public ExploreCommand() {
        super("explore");
    }

    @Override
    public void execute(String[] args) {
        Player player = Game.getPlayer();
        if (!makeSafe(args, player)) {
            return;
        }
        Tile curTile = Game.getGameMap().curZone(player.getX(), player.getY());
        if (curTile == null) {
            System.out.println("You are in an undefined area.");
            return;
        }
        try {
            String structure = args[1];
            if (curTile.getStructure().getName().equalsIgnoreCase(structure)) {
                player.exploreStructure(structure);
            } else {
                System.out.println("Structure name mismatch");
            }
        } catch (Exception e) {
            System.out.println("Structure unavailable");
        }
    }

    @Override
    public String getDescription() {
        return "explore [structure name] — explore a nearby structure in your current zone";
    }

    @Override
    public boolean makeSafe(String[] args, Player player) {
        if (args.length < 2) {
            System.out.println("Usage: " + getDescription());
            return false;
        }
        return playerBaseCheck(player);
    }
}

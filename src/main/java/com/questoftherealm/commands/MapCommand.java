package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.game.Game;
import com.questoftherealm.map.Map;

public class MapCommand extends Command {

    public MapCommand() {
        super("map");
    }

    @Override
    public void execute(String[] args) {
        Player player = Game.getPlayer();
        Map map = Game.getGameMap();
        if (map == null) {
            System.out.println("Game map unavailable");
            return;
        }
        if (!makeSafe(args, player)) {
            return;
        }
        System.out.print("      ");
        System.out.println("╔════════ MAP ══════╗");
        try {
            map.print();
        } catch (Exception e) {
            System.out.println("Something went wrong while printing the map");
        }
        System.out.print("      ");
        System.out.println("╚═══════════════════╝");
    }

    @Override
    public String getDescription() {
        return "map — displays the current map layout and your position";
    }

    @Override
    public boolean makeSafe(String[] args, Player player) {
        if (args.length != 1) {
            System.out.println("Usage: " + getDescription());
            return false;
        }
        return playerBaseCheck(player);
    }
}

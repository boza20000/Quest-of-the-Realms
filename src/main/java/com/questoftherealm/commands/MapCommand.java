package com.questoftherealm.commands;

import com.questoftherealm.game.Game;

public class MapCommand extends Command {

    public MapCommand() {
        super("map");
    }


    @Override
    public void execute(String[] args) {
        if(Game.getGameMap() == null){
            System.out.println("Game map unavailable");
            return;
        }
        if (args.length > 1) {
            System.out.println("Usage: map");
            return;
        }
        if (Game.getPlayer() == null) {
            System.out.println("Error: No player currently active.");
            return;
        }
        System.out.print("      ");
        System.out.println("╔════════ MAP ══════╗");
        try {
            Game.getGameMap().print();
        }
        catch (Exception e){
            System.out.println("Something went wrong while printing the map");
        }
        System.out.print("      ");
        System.out.println("╚═══════════════════╝");
    }

    @Override
    public String getDescription() {
        return "gives the map layout";
    }
}

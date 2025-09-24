package com.questoftherealm.commands;

import com.questoftherealm.game.Game;

public class MapCommand extends Command {

    public MapCommand() {
        super("map");
    }


    @Override
    public void execute(String[] args) {
        System.out.print("      ");
        System.out.println("╔════════ MAP ══════╗");
        Game.getGameMap().print();
        System.out.print("      ");
        System.out.println("╚═══════════════════╝");
    }

    @Override
    public String getDescription() {
        return "gives the map layout";
    }
}

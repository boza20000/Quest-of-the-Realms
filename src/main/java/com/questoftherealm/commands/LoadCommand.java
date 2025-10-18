package com.questoftherealm.commands;

import com.questoftherealm.game.Game;
import com.questoftherealm.game.LoadGame;

public class LoadCommand extends Command {

    public LoadCommand() {
        super("load");
    }

    @Override
    public String getDescription() {
        return "gives a list of saved games an a option to load one";
    }

    @Override
    public void execute(String[] args) {
        if (args.length > 2) {
            System.out.println("Usage: load [filename]");
            return;
        }
        if (Game.getPlayer() == null) {
            System.out.println("Error: No player currently active.");
            return;
        }
        LoadGame.loadGameSave(args[1]);
    }
}

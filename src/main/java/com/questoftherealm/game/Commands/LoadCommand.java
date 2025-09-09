package com.questoftherealm.game.Commands;

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
        LoadGame.loadGameSave(args[1]);
    }
}

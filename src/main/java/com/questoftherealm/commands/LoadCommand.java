package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.game.Game;
import com.questoftherealm.game.LoadGame;

public class LoadCommand extends Command {

    public LoadCommand() {
        super("load");
    }

    @Override
    public String getDescription() {
        return "load [save name] — loads a saved game by its name";
    }

    @Override
    public boolean makeSafe(String[] args, Player player) {
        if (args.length != 2) {
            System.out.println("Usage: " + getDescription());
            return false;
        }
        return true;
    }
    @Override
    public void execute(String[] args) {
        Player player = Game.getPlayer();
        if (!makeSafe(args, player)) {
            return;
        }
        LoadGame.loadGameSave(args[1]);
    }
}

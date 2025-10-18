package com.questoftherealm.commands;

import com.questoftherealm.exceptions.SaveError;
import com.questoftherealm.game.Game;
import com.questoftherealm.game.SaveGame;

import java.io.IOException;

public class SaveCommand extends Command {
    public SaveCommand() {
        super("save");
    }

    @Override
    public String getDescription() {
        return "saves the game progress /save saveName ";
    }

    @Override
    public void execute(String[] args) {
        if (args.length > 2) {
            System.out.println("Usage: save [filename]");
            return;
        }
        if (Game.getPlayer() == null) {
            System.out.println("Error: No player currently active.");
            return;
        }

        String fileName = args[1];
        if(fileName.isEmpty()){
            throw new SaveError("Save name is empty");
        }
        try {
            SaveGame.saveGame(fileName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

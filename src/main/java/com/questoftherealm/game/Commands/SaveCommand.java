package com.questoftherealm.game.Commands;

import com.questoftherealm.exceptions.SaveError;
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
        String fileName = args[1];
        if(fileName.isEmpty()){
            throw new SaveError("Save name is empty");
        }
        try{
            SaveGame.saveGame(fileName);
        }
        catch (IOException e){
            throw new SaveError("Something went wrong game not saved");
        }

    }
}

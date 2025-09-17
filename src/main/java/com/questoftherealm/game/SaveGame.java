package com.questoftherealm.game;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class SaveGame {
    private SaveGame(){}

    public static void saveGame(String saveName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File saveDir  = new File("saves");
        if(!saveDir.exists()){
            saveDir.mkdirs();
        }

        String path = saveName + ".json";
        try {
            File fileSaves = new File(saveDir, path);
            mapper.writerWithDefaultPrettyPrinter().writeValue(fileSaves, Game.getPlayer());
        }
        catch (Exception e){
            System.out.println("File save failed");
        }

    }

}

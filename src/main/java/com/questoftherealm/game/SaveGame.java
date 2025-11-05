package com.questoftherealm.game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.exceptions.SaveError;

import java.io.File;
import java.io.IOException;

public class SaveGame {
    public SaveGame() {}

    public void createSave(String saveName) {
        try {
            File saveDir = new File("saves");
            if (!saveDir.exists() && !saveDir.mkdirs()) {
                System.err.println("❌ Could not create save directory");
                return;
            }

            File fileSave = new File(saveDir, saveName + ".json");

            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);

            Player player = Game.getPlayer();
            player.trackPlayTime();
            mapper.writeValue(fileSave, player);

            System.out.println("✅ Game saved successfully → " + fileSave.getAbsolutePath());

        } catch (IOException e) {
            throw new SaveError("❌ Failed to save game: ");
        } catch (Exception e) {
            throw new SaveError("❌ Unexpected error while saving: ");
        }
    }
}

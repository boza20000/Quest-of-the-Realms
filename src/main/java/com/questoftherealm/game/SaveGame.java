package com.questoftherealm.game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.questoftherealm.characters.player.Player;

import java.io.File;
import java.io.IOException;

public class SaveGame {
    private SaveGame() {}

    public static void saveGame(String saveName) {
        try {
            // Ensure saves directory exists
            File saveDir = new File("saves");
            if (!saveDir.exists() && !saveDir.mkdirs()) {
                System.err.println("❌ Could not create save directory");
                return;
            }

            File fileSave = new File(saveDir, saveName + ".json");

            // Jackson mapper with pretty printing and type info support
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);

            // ✅ Serialize player with type metadata for quests/missions
            Player player = Game.getPlayer();
            mapper.writeValue(fileSave, player);

            System.out.println("✅ Game saved successfully → " + fileSave.getAbsolutePath());

        } catch (IOException e) {
            System.err.println("❌ Failed to save game: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ Unexpected error while saving: " + e);
        }
    }
}

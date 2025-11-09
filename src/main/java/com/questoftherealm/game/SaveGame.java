package com.questoftherealm.game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.exceptions.SaveError;
import com.questoftherealm.localization.MessageBundle;

import java.io.File;
import java.io.IOException;

public class SaveGame {
    public SaveGame() {}

    public void createSave(String saveName) {
        try {
            File saveDir = new File(MessageBundle.get(GameConstants.savesDirectory));
            if (!saveDir.exists() && !saveDir.mkdirs()) {
                System.err.println(MessageBundle.get("saveGame.error.notCreated"));
                return;
            }

            File fileSave = new File(saveDir, saveName + ".json");

            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);

            Player player = Game.getPlayer();
            player.trackPlayTime();
            mapper.writeValue(fileSave, player);

            System.out.println(MessageBundle.get("saveGame.save.successful",fileSave.getAbsolutePath()));

        } catch (IOException e) {
            throw new SaveError(MessageBundle.get("saveGame.save.failed",saveName));
        } catch (Exception e) {
            throw new SaveError(MessageBundle.get("saveGame.save.unexpectedly.failed",saveName));
        }
    }
}

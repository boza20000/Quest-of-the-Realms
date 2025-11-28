package com.questoftherealm.game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.exceptions.SaveError;

import java.io.File;
import java.io.IOException;

public class SaveGame {
    public SaveGame() {}

    public void createSave(String saveName,Player player,GameState state) {
        try {
            File saveDir = new File("saves");
            if (!saveDir.exists() && !saveDir.mkdirs()) {
                state.getGameServices().getOutput().println(state.getMessages().getBundle().get("saveGame.error.notCreated"));
                return;
            }

            File fileSave = new File(saveDir, saveName + ".json");

            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);

            player.trackPlayTime(state);
            mapper.writeValue(fileSave, player);

            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("saveGame.save.successful",fileSave.getAbsolutePath()));

        } catch (IOException e) {
            throw new SaveError(state.getMessages().getBundle().get("saveGame.save.failed",saveName));
        } catch (Exception e) {
            throw new SaveError(state.getMessages().getBundle().get("saveGame.save.unexpectedly.failed",saveName));
        }
    }
}

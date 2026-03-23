package com.questoftherealm.game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.exceptions.SaveError;
import com.questoftherealm.server.ServerLogger;

import java.io.File;
import java.io.IOException;

public class SaveGame {
    private final String SAVE_DIRECTORY;
    private final String MULTIPLAYER_SAVE_DIRECTORY;

    public SaveGame() {
        this.SAVE_DIRECTORY = "saves";
        this.MULTIPLAYER_SAVE_DIRECTORY = "server_saves";
    }

    public SaveGame(String fileSingle, String fileMultiplayer) {
        this.SAVE_DIRECTORY = fileSingle;
        this.MULTIPLAYER_SAVE_DIRECTORY = fileMultiplayer;
    }

    public void createSave(String saveName, Player player, GameState state) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            singlePlayerSave(state, player, saveName, mapper);

        } catch (IOException e) {
            ServerLogger.get().error("SaveGame: IOException while saving " + saveName, e);
            throw new SaveError(state.getMessages().getBundle().get("saveGame.save.failed", saveName));
        } catch (Exception e) {
            ServerLogger.get().error("SaveGame: Unexpected exception while saving " + saveName, e);
            throw new SaveError(state.getMessages().getBundle().get("saveGame.save.unexpectedly.failed", saveName));
        }
    }

    public void createSave(Player player, GameState state) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            makeMultiplayerSave(state, player, mapper);

        } catch (IOException e) {
            ServerLogger.get().error("SaveGame: IOException while saving multiplayer for player " + player.getName(), e);
            throw new SaveError(state.getMessages().getBundle().get("saveGame.save.failed.multiplayer", player.getName()));
        } catch (Exception e) {
            ServerLogger.get().error("SaveGame: Unexpected exception while saving multiplayer for player " + player.getName(), e);
            throw new SaveError(state.getMessages().getBundle().get("saveGame.save.unexpectedly.failed.multiplayer", player.getName()));
        }
    }

    private void singlePlayerSave(GameState state, Player player, String saveName, ObjectMapper mapper) throws IOException {
        File saveDir = new File(SAVE_DIRECTORY);
        saveFile(saveDir, player, saveName, mapper, state);
    }

    private void makeMultiplayerSave(GameState state, Player player, ObjectMapper mapper) throws IOException {
        File saveDir = new File(MULTIPLAYER_SAVE_DIRECTORY);
        File serverDir = new File(saveDir, state.getName());
        String fileNameSave = player.getName();
        saveFile(serverDir, player, fileNameSave, mapper, state);
    }

    private void saveFile(File saveDir, Player player, String fileNameSave, ObjectMapper mapper, GameState state) throws IOException {
        if (!saveDir.exists() && !saveDir.mkdirs()) {
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("saveGame.error.notCreated"));
            return;
        }
        File fileSave = new File(saveDir, fileNameSave + ".json");
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        player.trackPlayTime(state);
        mapper.writeValue(fileSave, player);

        state.getGameServices().getOutput().println(state.getMessages().getBundle().get("saveGame.save.successful", fileSave.getAbsolutePath()));
    }
}

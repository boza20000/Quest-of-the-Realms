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
        validateSinglePlayerInputs(saveName, player, state);
        validatePlayTime(player, state, true);
        player.trackPlayTime(state);
        
        try {
            singlePlayerSave(state, player, saveName);
        } catch (IOException e) {
            ServerLogger.get().error("SaveGame: IOException while saving " + saveName, e);
            throw new SaveError(state.getMessages().getBundle().get("saveGame.save.failed", saveName));
        } catch (Exception e) {
            ServerLogger.get().error("SaveGame: Unexpected exception while saving " + saveName, e);
            throw new SaveError(state.getMessages().getBundle().get("saveGame.save.unexpectedly.failed", saveName));
        }
    }

    public void createSave(Player player, GameState state) {
        validateMultiPlayerInputs(player, state);

        if (!isValidPlayTime(player)) {
            return;
        }
        
        player.trackPlayTime(state);
        
        try {
            makeMultiplayerSave(state, player);
        } catch (IOException e) {
            ServerLogger.get().error("SaveGame: IOException while saving multiplayer for player " + player.getName(), e);
            throw new SaveError(state.getMessages().getBundle().get("saveGame.save.failed.multiplayer", player.getName()));
        } catch (NullPointerException e) {
            ServerLogger.get().error("SaveGame: NullPointerException while saving multiplayer - player or state is null", e);
            throw new SaveError(state.getMessages().getBundle().get("saveGame.save.unexpectedly.failed.multiplayer", player.getName()));
        }
    }
    
    private void validateSinglePlayerInputs(String saveName, Player player, GameState state) {
        if (player == null || saveName == null || saveName.isBlank()) {
            throw new SaveError(state.getMessages().getBundle().get("saveGame.save.failed", saveName));
        }
    }

    private void validateMultiPlayerInputs(Player player, GameState state) {
        if (player == null || player.getName() == null || player.getName().isBlank()) {
            throw new SaveError(state.getMessages().getBundle().get("saveGame.save.unexpectedly.failed.multiplayer", "Unknown"));
        }
    }

    private void validatePlayTime(Player player, GameState state, boolean throwError) {
        if (!isValidPlayTime(player)) {
            String logMsg = String.format("SaveGame: Attempted to save single-player game with zero playtime for player: %s",
                    player.getName());
            ServerLogger.get().warn(logMsg);
            
            if (throwError) {
                throw new SaveError(state.getMessages().getBundle().get("saveGame.save.minPlaytimeRequired"));
            }
        }
    }

    private boolean isValidPlayTime(Player player) {
        return !(player.getPlayTime().hours() == 0 && player.getPlayTime().minutes() == 0);
    }
    
    private void singlePlayerSave(GameState state, Player player, String saveName) throws IOException {
        File saveDir = new File(SAVE_DIRECTORY);
        saveGameFile(saveDir, player, saveName, state);
    }

    private void makeMultiplayerSave(GameState state, Player player) throws IOException {
        File saveDir = new File(MULTIPLAYER_SAVE_DIRECTORY);
        File serverDir = new File(saveDir, state.getName());
        saveGameFile(serverDir, player, player.getName(), state);
    }

    private void saveGameFile(File saveDir, Player player, String fileName, GameState state) throws IOException {
        ensureSaveDirectory(saveDir, state);
        
        File fileSave = new File(saveDir, fileName + ".json");
        ObjectMapper mapper = createConfiguredMapper();
        trackAndWritePlayer(mapper, fileSave, player, state);
        logSuccess(fileSave, state);
    }

    private void ensureSaveDirectory(File saveDir, GameState state) {
        if (!saveDir.exists() && !saveDir.mkdirs()) {
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("saveGame.error.notCreated"));
        }
    }

    private ObjectMapper createConfiguredMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        return mapper;
    }

    private void trackAndWritePlayer(ObjectMapper mapper, File fileSave, Player player, GameState state) throws IOException {
        mapper.writeValue(fileSave, player);
    }

    private void logSuccess(File fileSave, GameState state) {
        state.getGameServices().getOutput().println(
                state.getMessages().getBundle().get("saveGame.save.successful", fileSave.getAbsolutePath())
        );
    }
}

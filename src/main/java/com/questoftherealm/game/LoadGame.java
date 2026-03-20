package com.questoftherealm.game;

import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.exceptions.CorruptedFileException;
import com.questoftherealm.exceptions.FileNotLoaded;
import com.questoftherealm.exceptions.SavesNotFound;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.items.Item;
import com.questoftherealm.items.ItemKeyDeserializer;
import com.questoftherealm.items.ItemRegistry;
import com.questoftherealm.server.ServerLogger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class LoadGame {
    private final String LOAD_DIRECTORY;
    private final String MULTIPLAYER_LOAD_DIRECTORY;

    public LoadGame() {
        this.LOAD_DIRECTORY = "saves";
        this.MULTIPLAYER_LOAD_DIRECTORY = "server_saves";
    }

    public LoadGame(String fileSingle, String fileMulti) {
        this.LOAD_DIRECTORY = fileSingle;
        this.MULTIPLAYER_LOAD_DIRECTORY = fileMulti;
    }

    public void loadGameSave(String filename, GameState state) {
        loadGameInformation(filename, state);
    }

    public void loadServerSave(GameState state, Player player) {
        try {
            loadServerGameInformation(state, player);
        } catch (FileNotFoundException e) {
            throw new FileNotLoaded(state.getMessages().getBundle().get("loadGame.file.notFound"));
        } catch (IOException e) {
            throw new CorruptedFileException(state.getMessages().getBundle().get("loadGame.file.corrupted"));
        }
    }

    private void loadServerGameInformation(GameState state, Player player) throws IOException {
        File directory = new File(MULTIPLAYER_LOAD_DIRECTORY);
        File serverFile = new File(directory, state.getName());
        File savedFile = new File(serverFile, player.getName() + ".json");

        if (savedFile.exists() && savedFile.isFile()) {

            ObjectMapper mapper = new ObjectMapper();
            mapper.setInjectableValues(new InjectableValues.Std().addValue(ItemRegistry.class.getName(), state.getItemRegistry()));

            SimpleModule module = new SimpleModule();
            module.addKeyDeserializer(Item.class, new ItemKeyDeserializer());
            mapper.registerModule(module);

            Player loaded = mapper.readValue(savedFile, Player.class);
            if (loaded.getQuestFactory() != null) {
                loaded.getQuestFactory().restoreAfterLoad(loaded, state);
            }
            state.addPlayer(loaded);
        } else {
            throw new FileNotFoundException(state.getMessages().getBundle().get("loadGame.file.notFound"));
        }
    }

    public void loadGameInformation(String filename, GameState state) {
        File directory = new File(LOAD_DIRECTORY);
        File savedFile = new File(directory, filename + ".json");

        if (!savedFile.exists() || !savedFile.isFile()) {
            throw new FileNotLoaded(state.getMessages().getBundle().get("loadGame.file.notFound"));
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setInjectableValues(new InjectableValues.Std().addValue(ItemRegistry.class.getName(), state.getItemRegistry()));

            SimpleModule module = new SimpleModule();
            module.addKeyDeserializer(Item.class, new ItemKeyDeserializer());
            mapper.registerModule(module);

            Player loaded = mapper.readValue(savedFile, Player.class);
            if (loaded.getQuestFactory() != null) {
                loaded.getQuestFactory().restoreAfterLoad(loaded, state);
            }
            for (Player existing : state.getActivePlayers()) {
                state.removePlayer(existing.getName());
            }
            state.addPlayer(loaded);
            for (Player player : state.getActivePlayers()) {
                player.move(player.getX(), player.getY());
            }
        } catch (IOException e) {
            throw new CorruptedFileException(state.getMessages().getBundle().get("loadGame.file.corrupted"));
        }
    }

    public void printSaves(GameState state) {
        try {
            File saveDir = new File(LOAD_DIRECTORY);
            Output output = state.getGameServices().getOutput();
            if (saveDir.exists() && saveDir.isDirectory()) {
                File[] files = saveDir.listFiles((dir, name) -> name.endsWith(".json"));
                if (files == null) {
                    output.println(state.getMessages().getBundle().get("loadGame.saves.dirMissing"));
                    return;
                }
                List<File> savedFiles = List.of(files);

                output.println(state.getMessages().getBundle().get("loadGame.saves.header"));
                if (savedFiles.isEmpty()) {
                    output.println(state.getMessages().getBundle().get("loadGame.saves.none"));
                    return;
                }
                for (File f : savedFiles) {
                    output.println(f.getName());
                }
                output.println(state.getMessages().getBundle().get("loadGame.saves.footer"));
                output.println(state.getMessages().getBundle().get("loadGame.saves.prompt"));
            } else {
                output.println(state.getMessages().getBundle().get("loadGame.saves.dirMissing"));
            }
        } catch (SecurityException e) {
            ServerLogger.get().error("LoadGame: Security exception accessing saves directory", e);
            throw new SavesNotFound(state.getMessages().getBundle().get("loadGame.saves.unavailable"));
        }
    }
}

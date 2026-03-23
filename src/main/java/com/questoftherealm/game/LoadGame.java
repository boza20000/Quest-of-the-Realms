package com.questoftherealm.game;

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
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Objects;

public class LoadGame {
    public LoadGame() {}

    public void loadGameSave(String filename, GameState state) {
        try {
            loadGameInformation(filename, state);
        } catch (FileNotLoaded e) {
            throw new CorruptedFileException(state.getMessages().getBundle().get("loadGame.file.corrupted"));
        }
    }

    public void loadGameInformation(String filename, GameState state) {
        try {
            File directory = new File("saves");
            File savedFile = new File(directory, filename + ".json");
            if (savedFile.exists() && savedFile.isFile()) {

                ObjectMapper mapper = new ObjectMapper();
                mapper.setInjectableValues(new com.fasterxml.jackson.databind.InjectableValues.Std()
                        .addValue(ItemRegistry.class.getName(), state.getItemRegistry()));

                SimpleModule module = new SimpleModule();
                module.addKeyDeserializer(Item.class, new ItemKeyDeserializer());
                mapper.registerModule(module);

                Player loaded = mapper.readValue(savedFile, Player.class);
                if (loaded.getQuestFactory() != null) {
                    loaded.getQuestFactory().restoreAfterLoad(loaded, state);
                }
                state.setPlayer(loaded);
                state.getPlayer().move(state.getPlayer().getX(),state.getPlayer().getY());

            } else {
                throw new FileNotFoundException(state.getMessages().getBundle().get("loadGame.file.notFound"));
            }
        } catch (Exception e) {
            throw new FileNotLoaded(state.getMessages().getBundle().get("loadGame.file.notLoaded"));
        }
    }

    public void printSaves(GameState state) {
        try {
            File saveDir = new File("saves");
            Output output = state.getGameServices().getOutput();
            if (saveDir.exists() && saveDir.isDirectory()) {
                List<File> savedFiles = List.of(
                        Objects.requireNonNull(saveDir.listFiles((dir, name) -> name.endsWith(".json")))
                );

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
        } catch (Exception e) {
            throw new SavesNotFound(state.getMessages().getBundle().get("loadGame.saves.unavailable"));
        }
    }
}

package com.questoftherealm.game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.exceptions.CorruptedFileException;
import com.questoftherealm.exceptions.FileNotLoaded;
import com.questoftherealm.exceptions.SavesNotFound;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.localization.MessageBundle;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Objects;

public class LoadGame {
    private Output output;
    private GameState state;
    public LoadGame(GameState state) {
        this.state = state;
        this.output = state.getGameServices().getOutput();
    }

    public void loadGameSave(String filename) {
        try {
            loadGameInformation(filename);
        } catch (FileNotLoaded e) {
            throw new CorruptedFileException(MessageBundle.get("loadGame.file.corrupted"));
        }
    }

    public void loadGameInformation(String filename) {
        try {
            File directory = new File("saves");
            File savedFile = new File(directory, filename + ".json");
            if (savedFile.exists() && savedFile.isFile()) {

                ObjectMapper mapper = new ObjectMapper();
                Player loaded = mapper.readValue(savedFile, Player.class);
                if (loaded.getQuestFactory() != null) {
                    loaded.getQuestFactory().restoreAfterLoad(loaded,state);
                }
                state.setPlayer(loaded);

            } else {
                throw new FileNotFoundException(MessageBundle.get("loadGame.file.notFound"));
            }
        } catch (Exception e) {
            throw new FileNotLoaded(MessageBundle.get("loadGame.file.notLoaded"));
        }
    }

    public void printSaves() {
        try {
            File saveDir = new File("saves");
            if (saveDir.exists() && saveDir.isDirectory()) {
                List<File> savedFiles = List.of(
                        Objects.requireNonNull(saveDir.listFiles((dir, name) -> name.endsWith(".json")))
                );
                output.println(MessageBundle.get("loadGame.saves.header"));
                if (savedFiles.isEmpty()) {
                    output.println(MessageBundle.get("loadGame.saves.none"));
                    return;
                }
                for (File f : savedFiles) {
                    output.println(f.getName());
                }
                output.println(MessageBundle.get("loadGame.saves.footer"));
                output.println(MessageBundle.get("loadGame.saves.prompt"));
            } else {
                output.println(MessageBundle.get("loadGame.saves.dirMissing"));
            }
        } catch (Exception e) {
            throw new SavesNotFound(MessageBundle.get("loadGame.saves.unavailable"));
        }
    }
}

package com.questoftherealm.game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.exceptions.FileNotLoaded;
import com.questoftherealm.exceptions.SavesNotFound;
import com.questoftherealm.localization.MessageBundle;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Objects;

public class LoadGame {
    public LoadGame() {}

    public void loadGameSave(String filename) {
        try {
            loadGameInformation(filename);
        } catch (FileNotLoaded e) {
            System.out.println(MessageBundle.get("loadGame.file.corrupted"));
            System.exit(0);
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
                    loaded.getQuestFactory().restoreAfterLoad(loaded);
                }
                Game.setPlayer(loaded);

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
                System.out.println(MessageBundle.get("loadGame.saves.header"));
                if (savedFiles.isEmpty()) {
                    System.out.println(MessageBundle.get("loadGame.saves.none"));
                    return;
                }
                for (File f : savedFiles) {
                    System.out.println(f.getName());
                }
                System.out.println(MessageBundle.get("loadGame.saves.footer"));
                System.out.println(MessageBundle.get("loadGame.saves.prompt"));
            } else {
                System.out.println(MessageBundle.get("loadGame.saves.dirMissing"));
            }
        } catch (Exception e) {
            throw new SavesNotFound(MessageBundle.get("loadGame.saves.unavailable"));
        }
    }
}

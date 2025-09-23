package com.questoftherealm.game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.exceptions.FileNotLoaded;
import com.questoftherealm.exceptions.SavesNotFound;
import com.questoftherealm.game.Commands.CommandFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Objects;

public class LoadGame {
    private LoadGame() {
    }

    public static void loadGameSave(String filename) {
        try {
            loadGameInformation(filename);
        } catch (FileNotLoaded e) {
            System.out.println("File not found or was corrupted");
            System.exit(0);
        }
    }

    public static void loadGameInformation(String filename) {
        try {
            File directory = new File("saves");
            File savedFile = new File(directory, filename + ".json");
            if (savedFile.exists() && savedFile.isFile()) {
                ObjectMapper mapper = new ObjectMapper();
                Game.setPlayer(mapper.readValue(savedFile, Player.class));
            } else {
                throw new FileNotFoundException();
            }
        } catch (Exception e) {
            throw new FileNotLoaded("File no loaded");
        }

    }

    public static void printSaves() {
        try {
            File saveDir = new File("saves");
            if (saveDir.exists() && saveDir.isDirectory()) {
                List<File> savedFiles = List.of(Objects.requireNonNull(saveDir.listFiles((dir, name) -> name.endsWith(".json"))));
                System.out.println("-----------Saves-----------");
                for (File f : savedFiles) {
                    System.out.println(f.getName());
                }
                System.out.println("---------------------------");
                System.out.println("Write the name of the save you want to load: ");
            } else {
                System.out.println("Saves not found directory does not exist or name mismatched");
            }
        } catch (Exception e) {
            throw new SavesNotFound("saves not available");
        }
    }
}

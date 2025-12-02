package com.questoftherealm.localization;

import com.questoftherealm.game.GameState;

import java.io.*;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.function.Supplier;

public class ArtLocalization {
    private final HashMap<String, Supplier<String>> asciArt;
    private GameState state;

    public ArtLocalization(GameState state) throws FileNotFoundException {
        this.state = state;
        asciArt = new HashMap<>();
        registerArt();
    }

    private void registerArt() {
        registerStartMenu();
        registerGameOver();
        registerQuestOfTheRealms();
        registerTimePlayed();
    }

    private void registerQuestOfTheRealms() {
        String key = "quest_of_the_realms";
        asciArt.put(key, () -> {
            try {
                return readArtFromFile(key);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void registerGameOver() {
        String key = "game_over";
        asciArt.put(key, () -> {
            try {
                return readArtFromFile(key);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void registerStartMenu() {
        String key = "start_menu";
        asciArt.put(key, () -> {
            try {
                return readArtFromFile(key);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void registerTimePlayed() {
        String key = "time_played";
        asciArt.put(key, () -> {
            try {
                return readArtFromFile(key);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private String readArtFromFile(String key) throws IOException {
        InputStream input = getClass().getClassLoader().getResourceAsStream("art.txt");
        if (input == null) {
            throw new FileNotFoundException("art.txt not found");
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
            StringBuilder artBuilder = new StringBuilder();
            boolean reading = false;
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.trim().equals("#" + key + "#")) {
                    reading = true;
                    continue;
                }

                if (line.trim().equals("#end#") && reading) {
                    break;
                }

                if (reading) {
                    artBuilder.append(line).append("\n");
                }
            }

            if (artBuilder.isEmpty()) {
                return "ART NOT FOUND FOR KEY: " + key;
            }
            return artBuilder.toString();
        }
    }

    public String getArt(String artName) throws IOException {
        Supplier<String> action = asciArt.get(artName);
        if (action == null) {
            state.getGameServices().getOutput().println("No ASCII art found for key: " + artName);
            return null;
        }
        return action.get();
    }

}

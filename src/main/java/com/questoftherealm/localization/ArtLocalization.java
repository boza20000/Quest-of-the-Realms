package com.questoftherealm.localization;

import com.questoftherealm.exceptions.ArtException;
import com.questoftherealm.game.GameState;
import com.questoftherealm.server.ServerLogger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.function.Supplier;

public class ArtLocalization {
    private final HashMap<String, Supplier<String>> asciArt;
    private GameState state;
    private final String resourceName;

    public ArtLocalization(GameState state) throws FileNotFoundException {
        this(state, "art.txt");
    }

    public ArtLocalization(GameState state, String resourceName) throws FileNotFoundException {
        this.state = state;
        this.resourceName = resourceName;
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
                ServerLogger.get().error("Failed to load ASCII art for key: " + key, e);
                throw new ArtException(e.getMessage());
            }
        });
    }

    private void registerGameOver() {
        String key = "game_over";
        asciArt.put(key, () -> {
            try {
                return readArtFromFile(key);
            } catch (IOException e) {
                ServerLogger.get().error("Failed to load ASCII art for key: " + key, e);
                throw new ArtException(e.getMessage());
            }
        });
    }

    private void registerStartMenu() {
        String key = "start_menu";
        asciArt.put(key, () -> {
            try {
                return readArtFromFile(key);
            } catch (IOException e) {
                ServerLogger.get().error("Failed to load ASCII art for key: " + key, e);
                throw new ArtException(e.getMessage());
            }
        });
    }

    private void registerTimePlayed() {
        String key = "time_played";
        asciArt.put(key, () -> {
            try {
                return readArtFromFile(key);
            } catch (IOException e) {
                ServerLogger.get().error("Failed to load ASCII art for key: " + key, e);
                throw new ArtException(e.getMessage());
            }
        });
    }

    private synchronized String readArtFromFile(String key) throws IOException {
        InputStream input = getClass().getClassLoader().getResourceAsStream(resourceName);
        if (input == null) {
            throw new FileNotFoundException(resourceName + " not found");
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
                return state.getMessages().getBundle().get("art.notFound", key);
            }
            return artBuilder.toString();
        }
    }

    public synchronized String getArt(String artName) throws IOException {
        Supplier<String> action = asciArt.get(artName);
        if (action == null) {
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("art.notFound", artName));
            return null;
        }
        return action.get();
    }

}

package com.questoftherealm.maps;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.questoftherealm.characters.player.Player;

import java.io.InputStream;

public class Map {

    private static Map instance;       // Singleton instance
    private Tile[][] gameMap;          // The map grid

    private Map() {
        loadMap();
    }

    public static Map getInstance() {
        if (instance == null) {
            instance = new Map();
        }
        return instance;
    }

    private void loadMap() {
        try (InputStream is = Map.class.getResourceAsStream("/map.json")) {
            ObjectMapper mapper = new ObjectMapper();
            gameMap = mapper.readValue(is, new TypeReference<Tile[][]>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Tile[][] getGameMap() {
        return gameMap;
    }

    public void movePlayer(Player player, int x, int y) {
        int newX = player.getX() + x;
        int newY = player.getY() + y;

        if (newX < 0 || newX >= gameMap.length || newY < 0 || newY >= gameMap[0].length) {
            System.out.println("Cannot move there! Out of bounds.");
            return;
        }
        player.move(newX, newY);
    }
}

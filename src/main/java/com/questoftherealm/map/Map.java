package com.questoftherealm.map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.exceptions.MapNotLoaded;
import com.questoftherealm.game.Game;
import com.questoftherealm.game.GameConstants;
import com.questoftherealm.localization.MessageBundle;

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
            if (is == null) {
                System.out.println(MessageBundle.get("map.notFound.error"));
                System.exit(0);
            }
            ObjectMapper mapper = new ObjectMapper();
            gameMap = mapper.readValue(is, Tile[][].class);
        } catch (Exception e) {
           throw new MapNotLoaded(MessageBundle.get("map.load.error"));
        }
    }

    public Tile[][] getGameMap() {
        return gameMap;
    }

    public void movePlayer(Player player, int x, int y) {
        player.setCurrentZone(gameMap[y][x].getDescription());

    }

    public void print(Player player) {
        for (int i = 0; i < gameMap.length; i++) {
            System.out.print("      ");
            System.out.print("║");
            for (int j = 0; j < gameMap[i].length; j++) {
                boolean isPlayerHere = (j == player.getX() && i == player.getY());
                Tile tile = gameMap[i][j];
                String symbol = getTileSymbol(tile);

                if (isPlayerHere) {
                    symbol = GameConstants.RED + "🧙" + GameConstants.RESET; // overlay
                }
                System.out.print(symbol);
            }
            System.out.print("║");
            System.out.println();
        }
    }

    private String getTileSymbol(Tile curTile) {
        return switch (curTile.getType()) {
            case GRASS -> GameConstants.GREEN + "\uD83D\uDFE9" + GameConstants.RESET;
            case FOREST -> GameConstants.DARK_GREEN + "\uD83C\uDF32" + GameConstants.RESET;
            case MOUNTAIN -> GameConstants.GRAY + "\uD83D\uDDFB" + GameConstants.RESET;
            case VILLAGE -> GameConstants.YELLOW + "\uD83C\uDFD8\uFE0F" + GameConstants.RESET;
            case CASTLE -> GameConstants.CYAN + "\uD83C\uDFF0" + GameConstants.RESET;
            case SWAMP -> GameConstants.MAGENTA + "\uD83D\uDFEB" + GameConstants.RESET;
            case WATER -> GameConstants.BLUE + "\uD83D\uDFE6" + GameConstants.RESET;
            //case QUEST_LOCATION -> GameConstants.RED + "❓" + GameConstants.RESET;
            default -> " ";
        };
    }

    public Tile curZone(int x, int y) {
        return gameMap[y][x];
    }
}

package com.questoftherealm.map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.exceptions.MapNotLoaded;
import com.questoftherealm.game.GameConstants;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.interfaces.Output;

import java.io.InputStream;


public class Map {
    private Tile[][] gameMap;

    public Map(GameState state) {
        loadMap(state);
    }

    private void loadMap(GameState state) {
        try (InputStream is = Map.class.getResourceAsStream("/map.json")) {
            if (is == null) {
                throw new MapNotLoaded(state.getMessages().getBundle().get("map.notFound.error"));
            }
            ObjectMapper mapper = new ObjectMapper();
            gameMap = mapper.readValue(is, Tile[][].class);
        } catch (Exception e) {
            throw new MapNotLoaded(state.getMessages().getBundle().get("map.load.error"));
        }
    }

    public Tile[][] getGameMap() {
        return gameMap;
    }

    public void movePlayer(Player player, int x, int y) {
        player.setCurrentZone(gameMap[y][x].getDescription());

    }

    public void print(Player player, GameState state) {
        Output output = state.getGameServices().getOutput();
        for (int i = 0; i < gameMap.length; i++) {
            output.print("      ");
            output.print(state.getMessages().getBundle().get("map.structure.symbol"));
            for (int j = 0; j < gameMap[i].length; j++) {
                boolean isPlayerHere = (j == player.getX() && i == player.getY());
                Tile tile = gameMap[i][j];
                String symbol = getTileSymbol(tile);

                if (isPlayerHere) {
                    symbol = GameConstants.RED + "🧙" + GameConstants.RESET; // overlay
                }
                output.print(symbol);
            }
            output.print(state.getMessages().getBundle().get("map.structure.symbol"));
            output.println();
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
        };
    }


    public Tile curZone(int x, int y) {
        return gameMap[y][x];
    }
}

package com.questoftherealm.map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.exceptions.MapNotLoaded;
import com.questoftherealm.game.GameConstants;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.interfaces.Output;

import java.io.InputStream;
import java.util.List;


public class WorldMap {
    private Tile[][] gameMap;
    private String space = "      ";

    public WorldMap(GameState state) {
        loadMap(state);
    }

    private void loadMap(GameState state) {
        try (InputStream is = WorldMap.class.getResourceAsStream("/map.json")) {
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

    public void print(GameState state) {
        Output output = state.getGameServices().getOutput();
        mapFrameStart(output);
        for (int i = 0; i < gameMap.length; i++) {
            output.print(space);
            output.print("║");
            for (int j = 0; j < gameMap[i].length; j++) {
                if (isPlayerHere(i,j,state)) {
                    output.print(GameConstants.RED + "🧙" + GameConstants.RESET);
                    continue;
                }
                output.print(getTileSymbol(gameMap[i][j]));
            }
            output.print("║" + System.lineSeparator());
        }
        mapFrameEnd(output);
    }

    private synchronized boolean isPlayerHere(int i, int j,GameState state) {
        List<Player> players =  state.getActivePlayers();
        for(Player player : players) {
            if (j == player.getX() && i == player.getY()){
                return true;
            }
        }
       return false;
    }

    private void mapFrameEnd(Output output) {
        output.print(space);
        output.print("╚");
        for (int i = 0; i <=GameConstants.MAP_END; i++) {
            output.print("══");
        }
        output.print("╝" + System.lineSeparator());
    }

    private void mapFrameStart(Output output) {
        output.print(space);
        output.print("╔");
        for (int i = 0; i <=GameConstants.MAP_END; i++) {
            output.print("══");
        }
        output.print("╗" + System.lineSeparator());
    }

    private String getTileSymbol(Tile curTile) {
        return switch (curTile.getType()) {
            case GRASS -> GameConstants.GREEN + "\uD83C\uDF31" + GameConstants.RESET;
            case FOREST -> GameConstants.DARK_GREEN + "\uD83C\uDF32" + GameConstants.RESET;
            case MOUNTAIN -> GameConstants.GRAY + "\uD83C\uDFD4\uFE0F" + GameConstants.RESET;
            case VILLAGE -> GameConstants.YELLOW + "\uD83D\uDED6" + GameConstants.RESET;
            case CASTLE -> GameConstants.CYAN + "\uD83C\uDFF0" + GameConstants.RESET;
            case SWAMP -> GameConstants.BROWN + "🪾" + GameConstants.RESET;
            case WATER -> GameConstants.BLUE + "\uD83C\uDF0A" + GameConstants.RESET;
        };
    }

    public Tile curZone(int x, int y) {
        return gameMap[y][x];
    }
}

package com.questoftherealm.game;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.items.ItemRegistry;
import com.questoftherealm.localization.LocalizationService;
import com.questoftherealm.map.WorldMap;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.questoftherealm.map.TriggerRegister;

public class GameState {
    private final String name;
    private boolean isPrivate = false;
    private final GameServices gameServices;
    private final Map<String, Player> activePlayers = new ConcurrentHashMap<>();
    private WorldMap gameMap;
    private final TriggerRegister triggerRegister;
    private boolean gameOver;
    private boolean isSimulation;
    private ServerClock clock;
    private final LocalizationService messages;
    private final ItemRegistry itemRegistry;

    public GameState(String name, GameServices services) {
        this.name = name;
        this.gameServices = services;
        this.gameMap = new WorldMap(this);
        this.messages = new LocalizationService();
        this.itemRegistry = new ItemRegistry(messages);
        this.triggerRegister = new TriggerRegister(this);
        this.gameOver = false;
        this.isSimulation = false;
        clock = new ServerClock();
    }

    public List<Player> getActivePlayers() {
        return activePlayers.values().stream().toList();
    }

    public Player getPlayer(String username) {
        return activePlayers.get(username);
    }

    public WorldMap getMap() {
        return gameMap;
    }

    public GameServices getGameServices() {
        return gameServices;
    }

    public TriggerRegister getTriggerRegister() {
        return triggerRegister;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public void setMap(WorldMap gameMap) {
        this.gameMap = gameMap;
    }

    public void setSimulation(boolean simulation) {
        isSimulation = simulation;
    }

    public boolean isSimulation() {
        return isSimulation;
    }

    public void addPlayer(Player loaded) {
        if (loaded != null) {
            if (isPrivate && activePlayers.isEmpty()) {
                activePlayers.put(loaded.getName(), loaded);
            } else if (!isPrivate) {
                activePlayers.put(loaded.getName(), loaded);
            }
        }
    }

    public ServerClock getClock() {
        return clock;
    }

    public void setClock(ServerClock clock) {
        this.clock = clock;
    }

    public LocalizationService getMessages() {
        return messages;
    }

    public void setGameMap(WorldMap gameMap) {
        this.gameMap = gameMap;
    }

    public ItemRegistry getItemRegistry() {
        return itemRegistry;
    }

    public void removePlayer(String username) {
        activePlayers.remove(username);
    }

    public void setPrivate(boolean prv) {
        isPrivate = prv;
    }

    public String getName() {
        return name;
    }
}

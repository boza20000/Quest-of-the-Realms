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
    private volatile WorldMap gameMap;
    private final TriggerRegister triggerRegister;
    private volatile boolean gameOver;
    private boolean isSimulation;
    private ServerClock clock;
    private final LocalizationService messages;
    private final ItemRegistry itemRegistry;
    private final ThreadLocal<GameServices> threadServices = new ThreadLocal<>();

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


    public void bindThreadServices(GameServices services) {
        threadServices.set(services);
    }

    public GameServices getGameServices() {
        GameServices ts = threadServices.get();
        return ts != null ? ts : gameServices;
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


    public TriggerRegister getTriggerRegister() {
        return triggerRegister;
    }

    public synchronized boolean isGameOver() {
        return gameOver;
    }

    public synchronized void setGameOver(boolean gameOver) {
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

    public synchronized void addPlayer(Player loaded) {
        if (loaded != null) {
            if (isPrivate && activePlayers.isEmpty()) {
                activePlayers.put(loaded.getName(), loaded);
            } else if (!isPrivate) {
                activePlayers.put(loaded.getName(), loaded);
            }
            if (!activePlayers.isEmpty()) {
                gameOver = false;
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

    public synchronized void removePlayer(String username) {
        activePlayers.remove(username);
    }

    public void setPrivate(boolean prv) {
        isPrivate = prv;
    }

    public String getName() {
        return name;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

}

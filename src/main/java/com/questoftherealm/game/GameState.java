package com.questoftherealm.game;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.items.ItemRegistry;
import com.questoftherealm.localization.LocalizationService;
import com.questoftherealm.map.Map;
import com.questoftherealm.map.TriggerRegister;

public class GameState {
    private final GameServices gameServices;
    private Player player;
    private Map gameMap;
    private final TriggerRegister triggerRegister;
    private boolean gameOver;
    private boolean isSimulation;
    private ServerClock clock;
    private final LocalizationService messages;
    private final ItemRegistry itemRegistry;

    public GameState(Player player, GameServices services) {
        this.player = player;
        this.gameServices = services;
        this.gameMap = new Map(this);
        //here can be added language in the LocalizationService constructor
        this.messages = new LocalizationService();
        this.itemRegistry = new ItemRegistry(messages);
        this.triggerRegister = new TriggerRegister(this);
        this.gameOver = false;
        this.isSimulation = false;
        clock = new ServerClock();
    }

    public Player getPlayer() {
        return player;
    }

    public Map getMap() {
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

    public void setMap(Map gameMap){
        this.gameMap = gameMap;
    }

    public void setSimulation(boolean simulation) {
        isSimulation = simulation;
    }

    public boolean isSimulation() {
        return isSimulation;
    }

    public void setPlayer(Player loaded) {
        this.player = loaded;
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

    public void setGameMap(Map gameMap) {
        this.gameMap = gameMap;
    }

    public ItemRegistry getItemRegistry() {
        return itemRegistry;
    }
}

package com.questoftherealm.game;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.map.Map;
import com.questoftherealm.map.TriggerRegister;

public class GameState {
    private final GameServices gameServices;
    private Player player;
    private Map gameMap;
    private final TriggerRegister triggerRegister;
    private boolean gameOver;
    private boolean isSimulation;

    public GameState(Player player, Output output, GameServices services) {
        this.player = player;
        this.gameServices = services;
        this.gameMap = new Map();
        this.triggerRegister = new TriggerRegister(this);
        gameOver = false;
        isSimulation = false;
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
        player = loaded;
    }
}

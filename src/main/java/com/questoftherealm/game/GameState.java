package com.questoftherealm.game;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.map.Map;
import com.questoftherealm.map.TriggerRegister;

public class GameState {
    private final GameServices gameServices;
    private Player player;
    private final Map gameMap;
    private final TriggerRegister triggerRegister;
    private boolean gameOver;

    public GameState(Player player, Output output, GameServices services) {
        this.player = player;
        this.gameServices = services;
        this.gameMap = new Map();
        this.triggerRegister = new TriggerRegister(this);
        gameOver = false;
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


    public void setPlayer(Player loaded) {
        player = loaded;
    }
}

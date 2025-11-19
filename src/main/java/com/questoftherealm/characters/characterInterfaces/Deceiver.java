package com.questoftherealm.characters.characterInterfaces;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.game.GameState;

public interface Deceiver {
    void pickpocket(Player player, Enemy enemy, GameState state);
}
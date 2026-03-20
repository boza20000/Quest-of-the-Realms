package com.questoftherealm.enemyEntities.EnemiesInterfaces;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.game.GameState;

public interface Fightable {
    void attack(Player player, GameState state);
    void takeDamage(int damage, GameState state);
    boolean isAlive();
}


package com.questoftherealm.enemyEntities.EnemiesInterfaces;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.game.GameState;

public interface Fightable {
    void attack(Player player, GameState state);   // can attack others
    void takeDamage(int damage, GameState state);     // can receive damage
    boolean isAlive();               // quick status check
}


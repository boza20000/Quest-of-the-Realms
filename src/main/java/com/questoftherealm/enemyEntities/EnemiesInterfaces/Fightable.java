package com.questoftherealm.enemyEntities.EnemiesInterfaces;

import com.questoftherealm.characters.player.Player;

public interface Fightable {
    void attack(Player player);   // can attack others
    void takeDamage(int damage);     // can receive damage
    boolean isAlive();               // quick status check
}


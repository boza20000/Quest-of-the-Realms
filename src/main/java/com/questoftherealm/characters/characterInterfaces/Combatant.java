package com.questoftherealm.characters.characterInterfaces;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.enemyEntities.Enemy;

public interface Combatant {
    void attack(Enemy target, Player player);

    void takeDamage(int damage);
}
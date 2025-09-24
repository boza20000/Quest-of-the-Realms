package com.questoftherealm.characters.characterInterfaces;

import com.questoftherealm.enemyEntities.Enemy;

public interface Combatant {
    void attack(Enemy target);
    void takeDamage(int damage);
    //void useMana(Item item);
}
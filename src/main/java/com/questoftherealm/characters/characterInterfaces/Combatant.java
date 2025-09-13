package com.questoftherealm.characters.characterInterfaces;

import com.questoftherealm.characters.EnemyEntities.Enemy;
import com.questoftherealm.characters.playerCharacters.Characters;

public interface Combatant {
    void attack(Enemy target);
    void takeDamage(int damage);
    //void useMana(Item item);
}
package com.questoftherealm.characters.interfaces;

import com.questoftherealm.characters.Characters;

public interface Combatant {
    void attack(Characters target);
    void takeDamage(int damage);
}
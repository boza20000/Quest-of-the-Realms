package com.questoftherealm.characters.interfaces;

import javax.xml.stream.events.Characters;

public interface Combatant {
    void attack(Characters target);
    void takeDamage(int damage);
}
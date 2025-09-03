package com.questoftherealm.characters.interfaces;

import javax.xml.stream.events.Characters;

public interface MonsterBehavior {
    void callHorde();
    void resurrect();
    void blockDamage(Characters target);
}

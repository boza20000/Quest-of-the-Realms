package com.questoftherealm.characters.interfaces;


import com.questoftherealm.characters.Characters;

public interface MonsterBehavior {
    void callHorde();
    void resurrect();
    void blockDamage(Characters target);
}

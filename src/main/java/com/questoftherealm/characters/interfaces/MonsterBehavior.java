package com.questoftherealm.characters.interfaces;


import com.questoftherealm.characters.playerCharacters.Characters;

public interface MonsterBehavior {
    void callHorde();
    void resurrect();
    void blockDamage(Characters target);
}

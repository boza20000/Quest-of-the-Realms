package com.questoftherealm.characters.characterInterfaces;


import com.questoftherealm.characters.playerCharacters.Characters;

public interface MonsterBehavior {
    void callHorde();
    void resurrect();
    void blockDamage(Characters target);
}

package com.questoftherealm.characters.playerCharacters;

import com.questoftherealm.characters.Characters;
import com.questoftherealm.characters.interfaces.MonsterBehavior;
import com.questoftherealm.items.Item;

public class Orc extends Characters implements MonsterBehavior {

    public Orc() {
        super(50, 3, 9, 3, 11, 2, 1, 2);
    }

    public Orc(int health, int mana, int attack, int defence, int armor, int charisma, int spells, int intelligence) {
        super(health, mana, attack, defence, armor, charisma, spells, intelligence);
    }

    @Override
    public void callHorde() {

    }

    @Override
    public void resurrect() {

    }

    @Override
    public void blockDamage(Characters target) {

    }

    @Override
    public void attack(Characters target) {

    }

    @Override
    public void equipItem(Item item) {

    }
}

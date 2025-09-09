package com.questoftherealm.characters.playerCharacters;

import com.questoftherealm.characters.interfaces.Deceiver;
import com.questoftherealm.characters.interfaces.Trader;
import com.questoftherealm.items.Item;
import com.questoftherealm.items.ItemRegistry;

public class Rogue extends Characters implements Trader, Deceiver {

    public Rogue() {
        super(35, 10, 7, 2, 7, 9, 3, 7);
    }

    public Rogue(int health, int mana, int attack, int defence, int armor, int charisma, int spells, int intelligence) {
        super(health, mana, attack, defence, armor, charisma, spells, intelligence);
    }

    @Override
    public void lie() {

    }

    @Override
    public void pickpocket() {

    }

    @Override
    public Item getDefaultWeapon() {
        return ItemRegistry.getItem("Iron Dagger");
    }

    @Override
    public void buyItem(Item item, int quantity) {

    }

    @Override
    public void sellItem(Item item, int quantity) {

    }
}

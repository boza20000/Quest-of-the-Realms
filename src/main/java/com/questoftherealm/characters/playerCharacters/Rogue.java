package com.questoftherealm.characters.playerCharacters;

import com.questoftherealm.characters.interfaces.Deceiver;
import com.questoftherealm.characters.interfaces.Trader;
import com.questoftherealm.items.Item;

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
    public void buyItem(Item item) {

    }

    @Override
    public void sellItem(Item item) {

    }

    @Override
    public void attack(Characters target) {

    }

    @Override
    public void openInventory() {

    }

    @Override
    public void addItem(Item item) {

    }

    @Override
    public void useItem(Item item) {

    }

    @Override
    public void equipItem(Item item) {

    }
}

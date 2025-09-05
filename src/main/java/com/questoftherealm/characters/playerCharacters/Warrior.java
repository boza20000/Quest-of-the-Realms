package com.questoftherealm.characters.playerCharacters;

import com.questoftherealm.characters.interfaces.Trader;
import com.questoftherealm.items.Item;

public class Warrior extends Characters implements Trader{

    public Warrior() {
        super(45, 5, 6, 4, 12, 5, 3, 4);
    }

    public Warrior(int health, int mana, int attack, int defence, int armor, int charisma, int spells, int intelligence) {
        super(health, mana, attack, defence, armor, charisma, spells, intelligence);
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

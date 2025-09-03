package com.questoftherealm.characters;

import com.questoftherealm.characters.interfaces.Combatant;
import com.questoftherealm.characters.interfaces.Explorer;
import com.questoftherealm.characters.interfaces.InventoryHandler;
import com.questoftherealm.characters.interfaces.Trader;
import com.questoftherealm.items.Item;

public class Warrior extends Characters implements Trader, Explorer, InventoryHandler, Combatant {
    public Warrior(int health, int mana, int attack, int defence, int armor, int charisma, int spells, int intelligence) {
        super(health, mana, attack, defence, armor, charisma, spells, intelligence);
    }

    @Override
    public void move(int x, int y) {

    }

    @Override
    public void openChest() {

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

    @Override
    public void buyItem(Item item) {

    }

    @Override
    public void sellItem(Item item) {

    }

    @Override
    public void attack(javax.xml.stream.events.Characters target) {

    }

    @Override
    public void takeDamage(int damage) {

    }
}

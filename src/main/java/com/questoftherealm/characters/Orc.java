package com.questoftherealm.characters;

import com.questoftherealm.characters.interfaces.Combatant;
import com.questoftherealm.characters.interfaces.Explorer;
import com.questoftherealm.characters.interfaces.InventoryHandler;
import com.questoftherealm.characters.interfaces.MonsterBehavior;
import com.questoftherealm.items.Item;

public class Orc extends Characters implements MonsterBehavior, Explorer, InventoryHandler, Combatant {
    private int attackIncrease;
    //spell weakness

    public Orc(int health, int mana, int attack, int defence, int armor, int charisma, int spells, int intelligence) {
        super(health, mana, attack, defence, armor, charisma, spells, intelligence);
    }

    @Override
    public void attack(javax.xml.stream.events.Characters target) {

    }

    @Override
    public void takeDamage(int damage) {

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
    public void callHorde() {

    }

    @Override
    public void resurrect() {

    }

    @Override
    public void blockDamage(javax.xml.stream.events.Characters target) {

    }

}

package com.questoftherealm.characters;

import com.questoftherealm.characters.interfaces.Combatant;
import com.questoftherealm.characters.interfaces.Explorer;
import com.questoftherealm.characters.interfaces.InventoryHandler;
import com.questoftherealm.characters.interfaces.SpellCaster;
import com.questoftherealm.spells.Spell;
import com.questoftherealm.items.Item;

public class Mage extends Characters implements SpellCaster, Explorer, InventoryHandler, Combatant {
    final private int spellIncrease = 35;
    final private int intelligenceIncrease = 45;
    final private int charismaIncrease = 15;


    public Mage(int health, int mana, int attack, int defence, int armor, int charisma, int spells, int intelligence) {
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
    public void castSpell(Spell spell, javax.xml.stream.events.Characters target) {

    }

    @Override
    public void attack(javax.xml.stream.events.Characters target) {

    }

    @Override
    public void takeDamage(int damage) {

    }

}

package com.questoftherealm.characters.playerCharacters;

import com.questoftherealm.characters.interfaces.SpellCaster;
import com.questoftherealm.items.ItemRegistry;
import com.questoftherealm.spells.Spell;
import com.questoftherealm.items.Item;

public class Mage extends Characters implements SpellCaster {

    public Mage() {
        super(28, 30, 3, 1, 4, 6, 10, 10);
    }

    public Mage(int health, int mana, int attack, int defence, int armor, int charisma, int spells, int intelligence) {
        super(health, mana, attack, defence, armor, charisma, spells, intelligence);
    }

    @Override
    public void castSpell(Spell spell, javax.xml.stream.events.Characters target) {

    }

    @Override
    public void attack(Characters target) {

    }

    @Override
    public void equipItem(Item item) {

    }

    @Override
    public Item getDefaultWeapon() {
        return ItemRegistry.getItem("Bronze Sword");
    }
}

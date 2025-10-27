package com.questoftherealm.characters.playerCharacters;

import com.questoftherealm.characters.characterInterfaces.SpellCaster;
import com.questoftherealm.items.ItemRegistry;
import com.questoftherealm.spells.Spell;
import com.questoftherealm.items.Item;

import static com.questoftherealm.characters.playerCharacters.CharacterConstants.*;

public class Mage extends Characters implements SpellCaster {

    public Mage() {
        super(MAGE_HEALTH,
                MAGE_MANA,
                MAGE_ATTACK,
                MAGE_DEFENCE,
                MAGE_ARMOR,
                MAGE_CHARISMA,
                MAGE_SPELLS,
                MAGE_INTELLIGENCE);
    }

    public Mage(int health, int mana, int attack, int defence, int armor, int charisma, int spells, int intelligence) {
        super(health, mana, attack, defence, armor, charisma, spells, intelligence);
    }

    @Override
    public void castSpell(Spell spell, Characters target) {
        target.takeDamage(spell.takePower());
    }

    @Override
    public Item getDefaultWeapon() {
        return ItemRegistry.getItem("Wooden Staff");
    }

    @Override
    public int getBaseAttack() {
        return MAGE_ATTACK;
    }

    @Override
    public int getBaseDefence() {
        return MAGE_DEFENCE;
    }

    @Override
    public int getMaxHealth() {
        return MAGE_HEALTH;
    }
}

package com.questoftherealm.characters.playerCharacters;

import com.questoftherealm.characters.characterInterfaces.MonsterBehavior;
import com.questoftherealm.items.Item;
import com.questoftherealm.items.ItemRegistry;

import static com.questoftherealm.characters.playerCharacters.CharacterConstants.*;

public class Orc extends Characters implements MonsterBehavior {

    public Orc() {
        super(ORC_HEALTH,
                ORC_MANA,
                ORC_ATTACK,
                ORC_DEFENCE,
                ORC_ARMOR,
                ORC_CHARISMA,
                ORC_SPELLS,
                ORC_INTELLIGENCE);
    }

    public Orc(int health, int mana, int attack, int defence, int armor, int charisma, int spells, int intelligence) {
        super(health, mana, attack, defence, armor, charisma, spells, intelligence);
    }

    @Override
    public void resurrect() {

    }

    @Override
    public Item getDefaultWeapon() {
        return ItemRegistry.getItem("Iron Axe");
    }

    @Override
    public int getBaseAttack() {
        return ORC_ATTACK;
    }

    @Override
    public int getBaseDefence() {
        return ORC_DEFENCE;
    }
}

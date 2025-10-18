package com.questoftherealm.characters.playerCharacters;

import com.questoftherealm.characters.characterInterfaces.Deceiver;
import com.questoftherealm.characters.characterInterfaces.Trader;
import com.questoftherealm.enemyEntities.entities.TraderNPC;
import com.questoftherealm.items.Item;
import com.questoftherealm.items.ItemRegistry;

import static com.questoftherealm.characters.playerCharacters.CharacterConstants.*;

public class Rogue extends Characters implements Deceiver {

    public Rogue() {
        super(ROGUE_HEALTH,
                ROGUE_MANA,
                ROGUE_ATTACK,
                ROGUE_DEFENCE,
                ROGUE_ARMOR,
                ROGUE_CHARISMA,
                ROGUE_SPELLS,
                ROGUE_INTELLIGENCE);
    }

    public Rogue(int health, int mana, int attack, int defence, int armor, int charisma, int spells, int intelligence) {
        super(health, mana, attack, defence, armor, charisma, spells, intelligence);
    }

    @Override
    public void pickpocket() {

    }

    @Override
    public Item getDefaultWeapon() {
        return ItemRegistry.getItem("Iron Dagger");
    }

    @Override
    public int getBaseAttack() {
        return ROGUE_ATTACK;
    }

    @Override
    public int getBaseDefence() {
        return ROGUE_DEFENCE;
    }

}

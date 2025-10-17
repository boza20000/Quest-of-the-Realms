package com.questoftherealm.enemyEntities.bosses;

import com.questoftherealm.characters.playerCharacters.Characters;
import com.questoftherealm.items.Item;
import com.questoftherealm.items.ItemDrop;
import com.questoftherealm.items.ItemEffect;
import java.util.HashMap;
import java.util.List;

public abstract class Boss extends Characters {
    protected String name;
    protected HashMap<ItemEffect, Item> armor;
    protected Item weapon;
    protected List<ItemDrop> loot;

    public Boss(
            int health, int mana, int attack, int defence, int armorValue,
            int charisma, int spells, int intelligence, String name,
            HashMap<ItemEffect, Item> armor, Item weapon, List<ItemDrop> loot) {

        super(health, mana, attack, defence, armorValue, charisma, spells, intelligence);
        this.name = name;
        this.armor = armor;
        this.weapon = weapon;
        this.loot = loot;
    }

    public abstract void superMove();
}

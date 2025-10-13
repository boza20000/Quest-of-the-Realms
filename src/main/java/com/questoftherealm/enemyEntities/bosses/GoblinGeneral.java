package com.questoftherealm.enemyEntities.bosses;
import com.questoftherealm.items.Item;
import com.questoftherealm.items.ItemDrop;
import com.questoftherealm.items.ItemEffect;
import com.questoftherealm.items.ItemRegistry;

import java.util.HashMap;
import java.util.List;

import static com.questoftherealm.characters.playerCharacters.CharacterConstants.*;
import static com.questoftherealm.characters.playerCharacters.CharacterConstants.GoblinGeneral_INTELLIGENCE;

public class GoblinGeneral extends Boss  {
    private static final String name = "Azok";
    private static final Item weapon  =  ItemRegistry.getItem("Big Battle Axe");

    public GoblinGeneral(){
        super(GoblinGeneral_HEALTH,
                GoblinGeneral_MANA,
                GoblinGeneral_ATTACK,
                GoblinGeneral_DEFENCE,
                GoblinGeneral_ARMOR,
                GoblinGeneral_CHARISMA,
                GoblinGeneral_SPELLS,
                GoblinGeneral_INTELLIGENCE,
                name,
                createArmor(),
                weapon,
                createLoot()
        );

    }

    private static HashMap<ItemEffect, Item> createArmor(){
        HashMap<ItemEffect, Item> armor = new HashMap<>();
        armor.put(ItemEffect.HELMET, ItemRegistry.getItem("Goblin general’s Helmet"));
        armor.put(ItemEffect.CHESTPLATE,ItemRegistry.getItem("Goblin general’s Chestplate") );
        armor.put(ItemEffect.BOOTS, null);
        return armor;
    }

    private static List<ItemDrop> createLoot() {
        return List.of(
                new ItemDrop(ItemRegistry.getItem("Goblin General’s Helmet"), 1),
                new ItemDrop(ItemRegistry.getItem("Goblin General’s Axe"), 1)
        );
    }

    @Override
    public void superMove() {
        int newHealth = getHealth() * 2;
        setHealth(newHealth);
    }

    @Override
    public Item getDefaultWeapon() {
        return  ItemRegistry.getItem("Big Battle Axe");
    }

    @Override
    public int getBaseAttack() {
        return GoblinGeneral_ATTACK;
    }

    @Override
    public int getBaseDefence() {
        return GoblinGeneral_DEFENCE;
    }

    public String getName() {
        return name;
    }
}

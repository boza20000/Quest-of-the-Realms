package com.questoftherealm.enemyEntities.bosses;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.game.GameState;
import com.questoftherealm.items.Item;
import com.questoftherealm.items.ItemDrop;
import com.questoftherealm.items.ItemEffect;
import com.questoftherealm.items.ItemRegistry;
import com.questoftherealm.localization.MessageBundle;

import java.util.HashMap;
import java.util.List;

import static com.questoftherealm.characters.playerCharacters.CharacterConstants.*;

public class GoblinGeneral extends Boss {
    private static final String NAME = MessageBundle.get("boss.goblinGeneral.name");
    private static final Item WEAPON = ItemRegistry.getItem("Big Battle Axe");

    public GoblinGeneral() {
        super(GoblinGeneral_HEALTH,
                GoblinGeneral_MANA,
                GoblinGeneral_ATTACK,
                GoblinGeneral_DEFENCE,
                GoblinGeneral_ARMOR,
                GoblinGeneral_CHARISMA,
                GoblinGeneral_SPELLS,
                GoblinGeneral_INTELLIGENCE,
                NAME,
                createArmor(),
                WEAPON,
                createLoot(),
                false);
    }

    private static HashMap<ItemEffect, Item> createArmor() {
        HashMap<ItemEffect, Item> armor = new HashMap<>();
        armor.put(ItemEffect.HELMET, ItemRegistry.getItem("Goblin general’s Helmet"));
        armor.put(ItemEffect.CHESTPLATE, ItemRegistry.getItem("Goblin general’s Chestplate"));
        armor.put(ItemEffect.BOOTS, null);
        return armor;
    }

    private static List<ItemDrop> createLoot() {
        return List.of(
                new ItemDrop(ItemRegistry.getItem("Goblin general’s Helmet"), 1),
                new ItemDrop(ItemRegistry.getItem("Big Battle Axe"), 1)
        );
    }

    @Override
    public void superMove(Player player,GameState state) {
        int newHealth = getHealth() * 2;
        setHealth(newHealth);
        state.getGameServices().getOutput().println(MessageBundle.get("boss.goblinGeneral.superMove", NAME));
    }

    @Override
    public Item getDefaultWeapon() {
        return WEAPON;
    }

    @Override
    public int getBaseAttack() {
        return GoblinGeneral_ATTACK;
    }

    @Override
    public int getBaseDefence() {
        return GoblinGeneral_DEFENCE;
    }

    @Override
    public int getMaxHealth() {
        return GoblinGeneral_HEALTH;
    }

    @Override
    public void activateAbility(Player player, Enemy enemy, GameState state) {
        superMove(player,state);
    }

    public String getName() {
        return NAME;
    }
}

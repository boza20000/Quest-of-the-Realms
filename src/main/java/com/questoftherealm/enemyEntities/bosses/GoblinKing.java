package com.questoftherealm.enemyEntities.bosses;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.game.Game;
import com.questoftherealm.game.GameConstants;
import com.questoftherealm.interaction.SlowPrinter;
import com.questoftherealm.items.Item;
import com.questoftherealm.items.ItemDrop;
import com.questoftherealm.items.ItemEffect;
import com.questoftherealm.items.ItemRegistry;
import com.questoftherealm.localization.MessageBundle;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static com.questoftherealm.characters.playerCharacters.CharacterConstants.*;

public class GoblinKing extends Boss {
    private static final String NAME = MessageBundle.get("boss.goblinKing.name");
    private static final Item WEAPON = ItemRegistry.getItem("Goblin King Sword");

    public GoblinKing() {
        super(GoblinKing_HEALTH,
                GoblinKing_MANA,
                GoblinKing_ATTACK,
                GoblinKing_DEFENCE,
                GoblinKing_ARMOR,
                GoblinKing_CHARISMA,
                GoblinKing_SPELLS,
                GoblinKing_INTELLIGENCE,
                NAME,
                createArmor(),
                WEAPON,
                createLoot());
    }

    private static HashMap<ItemEffect, Item> createArmor() {
        HashMap<ItemEffect, Item> armor = new HashMap<>();
        armor.put(ItemEffect.HELMET, ItemRegistry.getItem("Goblin king’s Crown"));
        armor.put(ItemEffect.CHESTPLATE, ItemRegistry.getItem("Goblin king’s Steel Chestplate"));
        armor.put(ItemEffect.BOOTS, null);
        return armor;
    }

    private static List<ItemDrop> createLoot() {
        return List.of(
                new ItemDrop(ItemRegistry.getItem("Goblin king’s Crown"), 1),
                new ItemDrop(ItemRegistry.getItem("Goblin King Sword"), 1)
        );
    }

    @Override
    public void superMove() {
        Random random = new Random();
        int roll = random.nextInt(100);
        SlowPrinter.slowPrint(MessageBundle.get("boss.goblinKing.attack.start"));

        if (GameConstants.GoblinKing_Percent_INSTAKILL <= roll) {
            SlowPrinter.slowPrint(MessageBundle.get("boss.goblinKing.attack.success"));
            Game.getPlayer().getPlayerCharacter().setHealth(0);
        } else {
            SlowPrinter.slowPrint(MessageBundle.get("boss.goblinKing.attack.fail"));
        }
    }

    @Override
    public Item getDefaultWeapon() {
        return WEAPON;
    }

    @Override
    public int getBaseAttack() {
        return GoblinKing_ATTACK;
    }

    @Override
    public int getBaseDefence() {
        return GoblinKing_DEFENCE;
    }

    @Override
    public int getMaxHealth() {
        return GoblinKing_HEALTH;
    }

    @Override
    public void activateAbility(Player player, Enemy enemy) {
        superMove();
    }

    public String getName() {
        return NAME;
    }
}

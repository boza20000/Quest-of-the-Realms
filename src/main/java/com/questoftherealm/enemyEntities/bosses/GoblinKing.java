package com.questoftherealm.enemyEntities.bosses;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.game.Game;
import com.questoftherealm.game.GameConstants;
import com.questoftherealm.game.GameLoop;
import com.questoftherealm.interaction.Console;
import com.questoftherealm.interaction.Interactions;
import com.questoftherealm.interaction.SlowPrinter;
import com.questoftherealm.items.Item;
import com.questoftherealm.items.ItemDrop;
import com.questoftherealm.items.ItemEffect;
import com.questoftherealm.items.ItemRegistry;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static com.questoftherealm.characters.playerCharacters.CharacterConstants.*;

public class GoblinKing extends Boss {
    private static final String name = "Tot";
    private static final Item weapon = ItemRegistry.getItem("Goblin King Sword");

    public GoblinKing() {
        super(GoblinKing_HEALTH,
                GoblinKing_MANA,
                GoblinKing_ATTACK,
                GoblinKing_DEFENCE,
                GoblinKing_ARMOR,
                GoblinKing_CHARISMA,
                GoblinKing_SPELLS,
                GoblinKing_INTELLIGENCE,
                name,
                createArmor(),
                weapon,
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
    //29% instant kill
    public void superMove() {
        Random random = new Random();
        int roll = random.nextInt(100);
        if (GameConstants.GoblinKing_Percent_INSTAKILL <= roll) {
            SlowPrinter.slowPrint("The Goblin king swing his sword with a huge strength.");
            SlowPrinter.slowPrint("You try to doge.. but unsuccessful...You get sliced");
            Game.getPlayer().getPlayerCharacter().setHealth(0);
        } else {
            SlowPrinter.slowPrint("The Goblin king swing his sword with a huge strength.");
            SlowPrinter.slowPrint("You almost get killed but you doge it");
        }
    }

    @Override
    public Item getDefaultWeapon() {
        return ItemRegistry.getItem("Goblin King Sword");
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
        return name;
    }
}

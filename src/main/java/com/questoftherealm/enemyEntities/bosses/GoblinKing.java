package com.questoftherealm.enemyEntities.bosses;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.game.GameConstants;
import com.questoftherealm.game.GameState;
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
    private ItemRegistry itemRegistry;
    private final String NAME = "Azok";

    public GoblinKing(GameState state) {
        super(GoblinKing_HEALTH,
                GoblinKing_MANA,
                GoblinKing_ATTACK,
                GoblinKing_DEFENCE,
                GoblinKing_ARMOR,
                GoblinKing_CHARISMA,
                GoblinKing_SPELLS,
                GoblinKing_INTELLIGENCE,
                null,
                null,
                null,
                null,
                false);
        itemRegistry = state.getItemRegistry();
        this.armor = createArmor();
        this.loot = createLoot();
        this.weapon = itemRegistry.getItem("Goblin King Sword");
    }

    private HashMap<ItemEffect, Item> createArmor() {
        HashMap<ItemEffect, Item> armor = new HashMap<>();
        armor.put(ItemEffect.HELMET, itemRegistry.getItem("Goblin king’s Crown"));
        armor.put(ItemEffect.CHESTPLATE, itemRegistry.getItem("Goblin king’s Steel Chestplate"));
        armor.put(ItemEffect.BOOTS, null);
        return armor;
    }

    private List<ItemDrop> createLoot() {
        return List.of(
                new ItemDrop(itemRegistry.getItem("Goblin king’s Crown"), 1),
                new ItemDrop(itemRegistry.getItem("Goblin King Sword"), 1)
        );
    }

    @Override
    public synchronized void superMove(Player player, GameState state) {
        SlowPrinter slowPrinter = new SlowPrinter(state);
        Random random = new Random();
        int roll = random.nextInt(100);
        slowPrinter.slowPrint(state.getMessages().getBundle().get("boss.goblinKing.attack.start"));

        if (GameConstants.GoblinKing_Percent_INSTAKILL <= roll) {
            slowPrinter.slowPrint(state.getMessages().getBundle().get("boss.goblinKing.attack.success"));
            player.getPlayerCharacter().setHealth(0);
        } else {
            slowPrinter.slowPrint(state.getMessages().getBundle().get("boss.goblinKing.attack.fail"));
        }
    }

    @Override
    public String getDefaultWeapon(GameState state) {
        return state.getMessages().getBundle().get(this.weapon.getName());
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
    public void activateAbility(Player player, Enemy enemy, GameState state) {
        superMove(player, state);
    }

    public String getName() {
        return NAME;
    }
}

package com.questoftherealm.enemyEntities;

import com.questoftherealm.items.ItemRegistry;
import com.questoftherealm.localization.MessageBundle;

import java.util.List;

public final class EnemyConstants {

    private EnemyConstants() {
    }

    public static final EnemyData GOBLIN = new EnemyData(
            MessageBundle.get("enemy.goblin.desc"),
            EnemyType.GOBLIN,
            30,
            5,
            2,
            List.of(ItemRegistry.getItem("Torn Leather Armor")),
            ItemRegistry.getItem("Iron Dagger"),
            List.of(
                    new Loot(ItemRegistry.getItem("Health Potion"), 0.3, 1, 1),
                    new Loot(ItemRegistry.getItem("Iron Dagger"), 0.15, 1, 1)
            ),
            false
    );

    public static final EnemyData BANDIT = new EnemyData(
            MessageBundle.get("enemy.bandit.desc"),
            EnemyType.BANDIT,
            50,
            6,
            3,
            List.of(ItemRegistry.getItem("Torn Leather Vest"), ItemRegistry.getItem("Ragged Boots")),
            ItemRegistry.getItem("Iron Dagger"),
            List.of(
                    new Loot(ItemRegistry.getItem("Health Potion"), 0.25, 1, 1),
                    new Loot(ItemRegistry.getItem("Iron Dagger"), 0.2, 1, 1)
            ),
            false
    );

    public static final EnemyData SKELETON = new EnemyData(
            MessageBundle.get("enemy.skeleton.desc"),
            EnemyType.SKELETON,
            40,
            7,
            4,
            List.of(ItemRegistry.getItem("Rusty Helmet")),
            ItemRegistry.getItem("Bronze Sword"),
            List.of(
                    new Loot(ItemRegistry.getItem("Bronze Sword"), 0.1, 1, 1)
            ),
            false
    );

    public static final EnemyData WOLF = new EnemyData(
            MessageBundle.get("enemy.wolf.desc"),
            EnemyType.WOLF,
            35,
            6,
            2,
            List.of(),
            null,
            List.of(
                    new Loot(ItemRegistry.getItem("Bronze Sword"), 0.1, 1, 1)
            ),
            false
    );

    public static final EnemyData GOBLIN_HORDE = new EnemyData(
            MessageBundle.get("enemy.goblinHorde.desc"),
            EnemyType.GOBLIN_HORDE,
            80,
            10,
            5,
            List.of(ItemRegistry.getItem("Torn Leather Armor")),
            ItemRegistry.getItem("Iron Sword"),
            List.of(
                    new Loot(ItemRegistry.getItem("Health Potion"), 0.35, 1, 2),
                    new Loot(ItemRegistry.getItem("Iron Sword"), 0.15, 1, 1)
            ),
            false
    );

    public static final EnemyData DARK_MAGE = new EnemyData(
            MessageBundle.get("enemy.darkMage.desc"),
            EnemyType.DARK_MAGE,
            60,
            12,
            3,
            List.of(ItemRegistry.getItem("Enchanted Robe"), ItemRegistry.getItem("Cloth Hood")),
            ItemRegistry.getItem("Mage’s Staff"),
            List.of(
                    new Loot(ItemRegistry.getItem("Mana Potion"), 0.4, 1, 2),
                    new Loot(ItemRegistry.getItem("Bronze Sword"), 0.1, 1, 1)
            ),
            false
    );

    public static final EnemyData GIANT_SPIDER = new EnemyData(
            MessageBundle.get("enemy.giantSpider.desc"),
            EnemyType.GIANT_SPIDER,
            70,
            10,
            5,
            List.of(),
            null,
            List.of(
                    new Loot(ItemRegistry.getItem("Bronze Sword"), 0.1, 1, 1)
            ),
            false
    );

    public static final EnemyData SPIRIT = new EnemyData(
            MessageBundle.get("enemy.spirit.desc"),
            EnemyType.LOST_SPIRIT,
            45,
            8,
            4,
            List.of(),
            null,
            List.of(
                    new Loot(ItemRegistry.getItem("Bronze Sword"), 0.1, 1, 1)
            ),
            false
    );

    public static final EnemyData TRAVELING_TRADER = new EnemyData(
            MessageBundle.get("enemy.trader.desc"),
            EnemyType.SUSPICIOUS_TRADER,
            40,
            2,
            1,
            List.of(),
            null,
            List.of(
                    new Loot(ItemRegistry.getItem("Health Potion"), 0.5, 1, 2),
                    new Loot(ItemRegistry.getItem("Mana Potion"), 0.4, 1, 2)
            ),
            false
    );
}

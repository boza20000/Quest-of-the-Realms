package com.questoftherealm.characters.EnemyEntities;

import com.questoftherealm.items.ItemRegistry;

import java.util.List;

public final class EnemyConstants {

    private EnemyConstants() {}

    public static final EnemyData GOBLIN = new EnemyData(
            "A sneaky goblin that lurks in the shadows",
            EnemyType.GOBLIN,
            30,
            5, // base attack (dagger stab or punch)


            2, // base defense
            List.of(ItemRegistry.getItem("Torn Leather Armor")),
            ItemRegistry.getItem("Iron Dagger"),
            List.of(
                    new Loot(ItemRegistry.getItem("Gold Coin"), 0.8, 1, 5),
                    new Loot(ItemRegistry.getItem("Health Potion"), 0.2, 1, 1)
            ),
            false
    );

    public static final EnemyData BANDIT = new EnemyData(
            "A rogue bandit",
            EnemyType.BANDIT,
            50,
            6, // stronger than goblin
            3,
            List.of(ItemRegistry.getItem("Torn Leather Vest"), ItemRegistry.getItem("Ragged Boots")),
            ItemRegistry.getItem("Iron Dagger"),
            List.of(
                    new Loot(ItemRegistry.getItem("Gold Coin"), 0.9, 2, 8),
                    new Loot(ItemRegistry.getItem("Iron Dagger"), 0.15, 1, 1)
            ),
            false
    );

    public static final EnemyData SKELETON = new EnemyData(
            "A reanimated skeleton",
            EnemyType.SKELETON,
            40,
            7,
            4,
            List.of(ItemRegistry.getItem("Rusty Helmet")),
            ItemRegistry.getItem("Bronze Sword"),
            List.of(
                    new Loot(ItemRegistry.getItem("Bone Fragment"), 0.7, 1, 3),
                    new Loot(ItemRegistry.getItem("Bronze Sword"), 0.1, 1, 1)
            ),
            false
    );

    public static final EnemyData WOLF = new EnemyData(
            "A feral wolf",
            EnemyType.WOLF,
            35,
            6, // bite
            2, // natural defense
            List.of(),
            null,
            List.of(
                    new Loot(ItemRegistry.getItem("Wolf Pelt"), 0.6, 1, 1),
                    new Loot(ItemRegistry.getItem("Wolf Fang"), 0.3, 1, 1)
            ),
            false
    );

    public static final EnemyData GOBLIN_HORDE = new EnemyData(
            "A group of goblins attacking together",
            EnemyType.GOBLIN_HORDE,
            80,
            10,
            5,
            List.of(ItemRegistry.getItem("Torn Leather Armor")),
            ItemRegistry.getItem("Iron Sword"),
            List.of(
                    new Loot(ItemRegistry.getItem("Gold Coin"), 1.0, 5, 15),
                    new Loot(ItemRegistry.getItem("Health Potion"), 0.25, 1, 2)
            ),
            false
    );

    public static final EnemyData DARK_MAGE = new EnemyData(
            "A dark mage with sinister powers",
            EnemyType.DARK_MAGE,
            60,
            12, // powerful spells
            3,
            List.of(ItemRegistry.getItem("Enchanted Robe"), ItemRegistry.getItem("Cloth Hood")),
            ItemRegistry.getItem("Mage’s Staff"),
            List.of(
                    new Loot(ItemRegistry.getItem("Mana Potion"), 0.4, 1, 2),
                    new Loot(ItemRegistry.getItem("Dark Tome"), 0.1, 1, 1)
            ),
            false
    );

    public static final EnemyData GOBLIN_GENERAL = new EnemyData(
            "The commanding goblin general",
            EnemyType.GOBLIN_GENERAL,
            100,
            15,
            7,
            List.of(ItemRegistry.getItem("Iron Helmet"), ItemRegistry.getItem("Iron Chestplate")),
            ItemRegistry.getItem("Orcish Axe"),
            List.of(
                    new Loot(ItemRegistry.getItem("Gold Coin"), 1.0, 10, 25),
                    new Loot(ItemRegistry.getItem("Orcish Axe"), 0.15, 1, 1)
            ),
            false
    );

    public static final EnemyData GIANT_SPIDER = new EnemyData(
            "A terrifying giant spider",
            EnemyType.GIANT_SPIDER,
            70,
            10, // venomous bite
            5,  // hard chitin armor
            List.of(),
            null,
            List.of(
                    new Loot(ItemRegistry.getItem("Spider Silk"), 0.5, 1, 3),
                    new Loot(ItemRegistry.getItem("Venom Sac"), 0.25, 1, 1)
            ),
            false
    );

    public static final EnemyData SPIRIT = new EnemyData(
            "A wandering lost spirit",
            EnemyType.LOST_SPIRIT,
            45,
            8,  // ethereal damage
            4,  // hard to hit
            List.of(),
            null,
            List.of(
                    new Loot(ItemRegistry.getItem("Ectoplasm"), 0.6, 1, 2),
                    new Loot(ItemRegistry.getItem("Cursed Relic"), 0.05, 1, 1)
            ),
            false
    );

    public static final EnemyData TRAVELING_TRADER = new EnemyData(
            "A traveling trader offering goods",
            EnemyType.TRAVELING_TRADER,
            40,
            2, // very weak, non-combat
            1,
            List.of(),
            null,
            List.of(
                    new Loot(ItemRegistry.getItem("Gold Coin"), 1.0, 17, 30), // drops a lot of money if killed
                    new Loot(ItemRegistry.getItem("Random Trade Goods"), 0.3, 1, 3)
            ),
            false
    );
}

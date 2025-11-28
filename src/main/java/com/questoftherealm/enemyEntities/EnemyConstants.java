package com.questoftherealm.enemyEntities;

import com.questoftherealm.game.GameState;
import com.questoftherealm.items.ItemRegistry;

import java.util.List;

public class EnemyConstants {
    public EnemyData createEnemy(EnemyType type, GameState state) {
        return switch (type) {
            case GOBLIN -> GOBLIN(state, state.getItemRegistry());
            case BANDIT -> BANDIT(state, state.getItemRegistry());
            case SKELETON -> SKELETON(state, state.getItemRegistry());
            case WOLF -> WOLF(state, state.getItemRegistry());
            case DARK_MAGE -> DARK_MAGE(state, state.getItemRegistry());
            case GIANT_SPIDER -> GIANT_SPIDER(state, state.getItemRegistry());
            case LOST_SPIRIT -> SPIRIT(state, state.getItemRegistry());
            case SUSPICIOUS_TRADER -> TRAVELING_TRADER(state, state.getItemRegistry());
        };
    }

    private EnemyData GOBLIN(GameState state, ItemRegistry registry) {
        return new EnemyData(
                state.getMessages().getBundle().get("enemy.goblin.desc"),
                EnemyType.GOBLIN,
                30,
                5,
                2,
                List.of(registry.getItem("Torn Leather Armor")),
                registry.getItem("Iron Dagger"),
                List.of(
                        new Loot(registry.getItem("Health Potion"), 0.3, 1, 1),
                        new Loot(registry.getItem("Iron Dagger"), 0.15, 1, 1)
                ),
                false
        );
    }

    private EnemyData BANDIT(GameState state, ItemRegistry registry) {
        return new EnemyData(
                state.getMessages().getBundle().get("enemy.bandit.desc"),
                EnemyType.BANDIT,
                50,
                6,
                3,
                List.of(registry.getItem("Torn Leather Vest"), registry.getItem("Ragged Boots")),
                registry.getItem("Iron Dagger"),
                List.of(
                        new Loot(registry.getItem("Health Potion"), 0.25, 1, 1),
                        new Loot(registry.getItem("Iron Dagger"), 0.2, 1, 1)
                ),
                false
        );
    }

    private EnemyData SKELETON(GameState state, ItemRegistry registry) {
        return new EnemyData(
                state.getMessages().getBundle().get("enemy.skeleton.desc"),
                EnemyType.SKELETON,
                40,
                7,
                4,
                List.of(registry.getItem("Rusty Helmet")),
                registry.getItem("Bronze Sword"),
                List.of(
                        new Loot(registry.getItem("Bronze Sword"), 0.1, 1, 1)
                ),
                false
        );
    }

    private EnemyData WOLF(GameState state, ItemRegistry registry) {
        return new EnemyData(
                state.getMessages().getBundle().get("enemy.wolf.desc"),
                EnemyType.WOLF,
                35,
                6,
                2,
                List.of(),
                null,
                List.of(
                        new Loot(registry.getItem("Bronze Sword"), 0.1, 1, 1)
                ),
                false
        );
    }

    private EnemyData DARK_MAGE(GameState state, ItemRegistry registry) {
        return new EnemyData(
                state.getMessages().getBundle().get("enemy.darkMage.desc"),
                EnemyType.DARK_MAGE,
                60,
                12,
                3,
                List.of(registry.getItem("Enchanted Robe"), registry.getItem("Cloth Hood")),
                registry.getItem("Mage’s Staff"),
                List.of(
                        new Loot(registry.getItem("Mana Potion"), 0.4, 1, 2),
                        new Loot(registry.getItem("Bronze Sword"), 0.1, 1, 1)
                ),
                false
        );
    }

    private EnemyData GIANT_SPIDER(GameState state, ItemRegistry registry) {
        return new EnemyData(
                state.getMessages().getBundle().get("enemy.giantSpider.desc"),
                EnemyType.GIANT_SPIDER,
                70,
                10,
                5,
                List.of(),
                null,
                List.of(
                        new Loot(registry.getItem("Bronze Sword"), 0.1, 1, 1)
                ),
                false
        );
    }

    private EnemyData SPIRIT(GameState state, ItemRegistry registry) {
        return new EnemyData(
                state.getMessages().getBundle().get("enemy.spirit.desc"),
                EnemyType.LOST_SPIRIT,
                45,
                8,
                4,
                List.of(),
                null,
                List.of(
                        new Loot(registry.getItem("Bronze Sword"), 0.1, 1, 1)
                ),
                false
        );
    }

    public EnemyData TRAVELING_TRADER(GameState state, ItemRegistry registry) {
        return new EnemyData(
                state.getMessages().getBundle().get("enemy.trader.desc"),
                EnemyType.SUSPICIOUS_TRADER,
                40,
                2,
                1,
                List.of(),
                null,
                List.of(
                        new Loot(registry.getItem("Health Potion"), 0.5, 1, 2),
                        new Loot(registry.getItem("Mana Potion"), 0.4, 1, 2)
                ),
                false
        );
    }
}
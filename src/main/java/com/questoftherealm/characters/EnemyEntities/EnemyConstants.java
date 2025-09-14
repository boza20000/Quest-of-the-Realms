package com.questoftherealm.characters.EnemyEntities;

import java.util.List;

public final class EnemyConstants {

    private EnemyConstants() {
    } // prevent instantiation

    public static final EnemyData GOBLIN = new EnemyData(
            "A sneaky goblin that lurks in the shadows",
            EnemyType.GOBLIN,
            30,
            List.of(), // armor
            null, // weapon
            false
    );

    public static final EnemyData BANDIT = new EnemyData(
            "A rogue bandit",
            EnemyType.BANDIT,
            50,
            List.of(),
            null, // weapon
            false
    );

    public static final EnemyData SKELETON = new EnemyData(
            "A reanimated skeleton",
            EnemyType.SKELETON,
            40,
            List.of(),
            null, // weapon
            false
    );

    public static final EnemyData WOLF = new EnemyData(
            "A feral wolf",
            EnemyType.WOLF,
            35,
            List.of(),
            null, // weapon
            false
    );

    public static final EnemyData GOBLIN_HORDE = new EnemyData(
            "A group of goblins attacking together",
            EnemyType.GOBLIN_HORDE,
            80,
            List.of(),
            null, // weapon
            false
    );

    public static final EnemyData DARK_MAGE = new EnemyData(
            "A dark mage with sinister powers",
            EnemyType.DARK_MAGE,
            60,
            List.of(),
            null, // weapon
            false
    );

    public static final EnemyData GOBLIN_GENERAL = new EnemyData(
            "The commanding goblin general",
            EnemyType.GOBLIN_GENERAL,
            100,
            List.of(),
            null, // weapon
            false
    );

    public static final EnemyData GIANT_SPIDER = new EnemyData(
            "A terrifying giant spider",
            EnemyType.GIANT_SPIDER,
            70,
            List.of(),
            null, // weapon
            false
    );

    public static final EnemyData SPIRIT = new EnemyData(
            "A wandering lost spirit",
            EnemyType.LOST_SPIRIT,
            45,
            List.of(),
            null, // weapon
            false
    );

    public static final EnemyData TRAVELING_TRADER = new EnemyData(
            "A traveling trader offering goods",
            EnemyType.TRAVELING_TRADER,
            40,
            List.of(),
            null, // weapon
            false
    );


}

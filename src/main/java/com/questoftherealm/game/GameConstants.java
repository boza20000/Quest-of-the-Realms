package com.questoftherealm.game;

import java.util.List;
import java.util.Random;

public final class GameConstants {
    public static final String PLAYER_SPAWN = "Castle";
    private static final Random random = new Random();
    private GameConstants() {
    }
    public static final int MAX_HEALTH = 100;
    public static final int MAX_MANA = 50;
    public static final int MAX_ATTACK = 30;
    public static final int MAX_DEFENCE = 10;
    public static final int MAX_ARMOR = 30;
    public static final int MAX_CHARISMA = 10;
    public static final int MAX_SPELLS = 10;
    public static final int MAX_INTELLIGENCE = 10;

    public static final String RESET = "\u001B[0m";
    public static final String GREEN = "\u001B[32m";   // Grass
    public static final String DARK_GREEN = "\u001B[32;1m"; // Forest
    public static final String GRAY = "\u001B[37m";    // Mountain
    public static final String YELLOW = "\u001B[33m";  // Village
    public static final String CYAN = "\u001B[36m";    // Castle
    public static final String MAGENTA = "\u001B[35m"; // Swamp
    public static final String BLUE = "\u001B[34m";    // Water/Lake
    public static final String RED = "\u001B[31m";     // Quest

    public static final int ONE_ENEMY_CHANCE = 65;
    public static final int TWO_ENEMY_CHANCE = 35;
    public static final int THREE_ENEMY_CHANCE = 15;
    public static final int MAX_ENEMIES = 3;
    public static final int MAX_ITEM_DROPS = 4;
    public static final int MAX_ITEMS_IN_STACK = 20;
    public static final int DELAY_MS = 40;


    public static final Position PLAYER_START = new Position(1, 6);
    public static final Position Castle = new Position(1, 6);
    public static final Position NorthVillage_1 = new Position(1, 1);
    public static final Position NorthVillage_2 = new Position(2, 5);

    public static final List<Position> North_Forest = List.of(
            new Position(0, 3),
            new Position(0, 4),
            new Position(1, 3),
            new Position(1, 4),
            new Position(1, 5),
            new Position(2, 3),
            new Position(2, 4)
    );
    public static final Position Goblin_Camp = North_Forest.get(random.nextInt(North_Forest.size()));

    public static final int North_Y = 2;
    public static final int MAP_END = 8;
    public static final int MAP_START = 0;
    public static final int MAX_ITEMS_IN_INVENTORY = 15;
    public static final int MAX_GOLD = 100;
    public static final int MAX_EXP_PER_LEVEL = 100;
    public static final Position SouthVillage_1 = new Position(5,6);
    public static final Position SouthVillage_2 = new Position(2,5);
    public static final Position MagesOutPost = new Position(2,7);
    public static final Position Battlefield = new Position(1,4);
    public static final Position FarNorthMountain = new Position(2,0);
    public static int GoblinKing_Percent_INSTAKILL = 29;
    public static final int START_LEVEL = 1;
}


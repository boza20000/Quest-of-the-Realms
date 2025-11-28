package com.questoftherealm.enemyEntities;

import com.questoftherealm.enemyEntities.entities.*;
import com.questoftherealm.game.GameState;

public class EnemyFactory {
    private static final EnemyConstants ENEMY_CONSTANTS_FACTORY = new EnemyConstants();

    public static Enemy createEnemy(EnemyType type, GameState state) {
        return switch (type) {
            case GOBLIN -> new Goblin(state, ENEMY_CONSTANTS_FACTORY);
            case BANDIT -> new Bandit(state, ENEMY_CONSTANTS_FACTORY);
            case SKELETON -> new Skeleton(state, ENEMY_CONSTANTS_FACTORY);
            case WOLF -> new Wolf(state, ENEMY_CONSTANTS_FACTORY);
            case DARK_MAGE -> new DarkMage(state, ENEMY_CONSTANTS_FACTORY);
            case GIANT_SPIDER -> new GiantSpider(state, ENEMY_CONSTANTS_FACTORY);
            case LOST_SPIRIT -> new Spirit(state, ENEMY_CONSTANTS_FACTORY);
            case SUSPICIOUS_TRADER -> new SuspiciousTrader(state, ENEMY_CONSTANTS_FACTORY);
        };
    }
}

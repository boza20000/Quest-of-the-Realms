package com.questoftherealm.enemyEntities;

import com.questoftherealm.enemyEntities.entities.*;

public class EnemyFactory {
    public static Enemy createEnemy(EnemyType type) {
       return switch (type) {
            case GOBLIN -> new Goblin();
            case BANDIT -> new Bandit();
            case SKELETON -> new Skeleton();
            case WOLF -> new Wolf();
            case GOBLIN_HORDE -> new GoblinHorde();
            case DARK_MAGE -> new DarkMage();
            case GIANT_SPIDER -> new GiantSpider();
            case LOST_SPIRIT -> new Spirit();
            case SUSPICIOUS_TRADER -> new SuspiciousTrader();
        };
    }
}

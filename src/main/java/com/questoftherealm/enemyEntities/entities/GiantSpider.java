package com.questoftherealm.enemyEntities.entities;

import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.enemyEntities.EnemyConstants;
import com.questoftherealm.items.ItemDrop;

import java.util.List;

public class GiantSpider extends Enemy {
    public GiantSpider() {
        super(EnemyConstants.GIANT_SPIDER);
    }

    @Override
    public List<ItemDrop> dropLoot() {
        return List.of();
    }
}

package com.questoftherealm.enemyEntities.entities;

import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.enemyEntities.EnemyConstants;
import com.questoftherealm.items.ItemDrop;

import java.util.List;

public class GoblinHorde extends Enemy {
    public GoblinHorde() {
        super(EnemyConstants.GOBLIN);
    }

    @Override
    public List<ItemDrop> dropLoot() {
        return List.of();
    }
}

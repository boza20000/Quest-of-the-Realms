package com.questoftherealm.enemyEntities.entities;

import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.enemyEntities.EnemyConstants;
import com.questoftherealm.items.ItemDrop;

import java.util.List;

public class Spirit extends Enemy {

    public Spirit() {
        super(EnemyConstants.SPIRIT);
    }
    @Override
    public List<ItemDrop> dropLoot() {
        return List.of();
    }
}

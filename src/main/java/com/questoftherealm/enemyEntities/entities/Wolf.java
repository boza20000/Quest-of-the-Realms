package com.questoftherealm.enemyEntities.entities;

import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.enemyEntities.EnemyConstants;
import com.questoftherealm.items.ItemDrop;

import java.util.List;

public class Wolf extends Enemy {
    public Wolf(){
        super(EnemyConstants.WOLF);
    }


    @Override
    public List<ItemDrop> dropLoot() {
        return List.of();
    }
}

package com.questoftherealm.enemyEntities.entities;

import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.enemyEntities.EnemyConstants;
import com.questoftherealm.enemyEntities.EnemyType;
import com.questoftherealm.game.GameState;
import com.questoftherealm.items.ItemDrop;

import java.util.List;

public class Wolf extends Enemy {
    public Wolf(GameState state, EnemyConstants constants){
        super(constants.createEnemy(EnemyType.WOLF,state));
    }


    @Override
    public List<ItemDrop> dropLoot() {
        return List.of();
    }
}

package com.questoftherealm.enemyEntities.entities;

import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.enemyEntities.EnemyConstants;
import com.questoftherealm.enemyEntities.EnemyType;
import com.questoftherealm.game.GameState;
import com.questoftherealm.items.ItemDrop;

import java.util.List;

public class Spirit extends Enemy {
    public Spirit(GameState state, EnemyConstants constants){
        super(constants.createEnemy(EnemyType.LOST_SPIRIT,state));
    }
    @Override
    public List<ItemDrop> dropLoot() {
        return List.of();
    }
}

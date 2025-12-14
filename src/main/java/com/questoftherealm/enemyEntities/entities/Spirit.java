package com.questoftherealm.enemyEntities.entities;

import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.enemyEntities.EnemyConstants;
import com.questoftherealm.enemyEntities.EnemyType;
import com.questoftherealm.game.GameState;

public class Spirit extends Enemy {
    public Spirit(GameState state, EnemyConstants constants){
        super(constants.createEnemy(EnemyType.LOST_SPIRIT,state));
    }
}

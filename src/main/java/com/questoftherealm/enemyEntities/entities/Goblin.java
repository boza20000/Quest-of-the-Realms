package com.questoftherealm.enemyEntities.entities;

import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.enemyEntities.EnemyConstants;
import com.questoftherealm.enemyEntities.EnemyType;
import com.questoftherealm.game.GameState;

public class Goblin extends Enemy {
    public Goblin(GameState state, EnemyConstants constants){
        super(constants.createEnemy(EnemyType.GOBLIN,state));
    }

}

package com.questoftherealm.enemyEntities.entities;

import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.enemyEntities.EnemyConstants;
import com.questoftherealm.enemyEntities.EnemyType;
import com.questoftherealm.game.GameState;

public class DarkMage extends Enemy  {
    public DarkMage(GameState state, EnemyConstants constants){
        super(constants.createEnemy(EnemyType.DARK_MAGE,state));
    }
}

package com.questoftherealm.enemyEntities.entities;

import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.enemyEntities.EnemyConstants;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.enemyEntities.EnemyType;
import com.questoftherealm.game.GameState;
import com.questoftherealm.items.Item;
import com.questoftherealm.items.ItemDrop;

import java.util.List;

public class SuspiciousTrader extends Enemy {
    public SuspiciousTrader(GameState state, EnemyConstants constants){
        super(constants.createEnemy(EnemyType.SUSPICIOUS_TRADER,state));
    }

    @Override
    public List<ItemDrop> dropLoot() {
        return List.of();
    }

}

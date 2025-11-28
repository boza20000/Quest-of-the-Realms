package com.questoftherealm.enemyEntities.entities;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.enemyEntities.EnemyConstants;
import com.questoftherealm.enemyEntities.EnemyType;
import com.questoftherealm.game.GameState;
import com.questoftherealm.items.ItemDrop;

import java.util.List;

public class DarkMage extends Enemy  {
    public DarkMage(GameState state, EnemyConstants constants){
        super(constants.createEnemy(EnemyType.DARK_MAGE,state));
    }
    @Override
    public List<ItemDrop> dropLoot() {
        return List.of();
    }


}

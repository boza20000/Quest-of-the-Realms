package com.questoftherealm.enemyEntities.entities;

import com.questoftherealm.enemyEntities.EnemiesInterfaces.Talkable;
import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.enemyEntities.EnemyConstants;
import com.questoftherealm.items.ItemDrop;

import java.util.List;

public class Goblin extends Enemy implements Talkable {
    public Goblin(){
        super(EnemyConstants.GOBLIN);
    }

    @Override
    public List<ItemDrop> dropLoot() {
        return List.of();
    }

    @Override
    public void talk() {

    }
}

package com.questoftherealm.characters.EnemyEntities.entities;

import com.questoftherealm.characters.EnemiesInterfaces.Talkable;
import com.questoftherealm.characters.EnemyEntities.Enemy;
import com.questoftherealm.characters.EnemyEntities.EnemyConstants;
import com.questoftherealm.items.ItemDrop;

import java.util.List;

public class Bandit extends Enemy implements Talkable {
    public Bandit(){
        super(EnemyConstants.BANDIT);
    }

    @Override
    public List<ItemDrop> dropLoot() {
        return List.of();
    }

    @Override
    public void talk() {

    }
}

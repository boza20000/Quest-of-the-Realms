package com.questoftherealm.characters.EnemyEntities;

import com.questoftherealm.characters.EnemiesInterfaces.Talkable;
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

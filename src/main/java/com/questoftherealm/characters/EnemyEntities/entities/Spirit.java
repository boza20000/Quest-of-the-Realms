package com.questoftherealm.characters.EnemyEntities.entities;

import com.questoftherealm.characters.EnemyEntities.Enemy;
import com.questoftherealm.characters.EnemyEntities.EnemyConstants;
import com.questoftherealm.items.ItemDrop;

import java.util.List;

public class Spirit extends Enemy {

    public Spirit() {
        super(EnemyConstants.SPIRIT);
    }
    @Override
    public List<ItemDrop> dropLoot() {
        return List.of();
    }
}

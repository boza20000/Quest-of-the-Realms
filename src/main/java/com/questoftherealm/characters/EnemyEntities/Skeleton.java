package com.questoftherealm.characters.EnemyEntities;

import com.questoftherealm.items.ItemDrop;

import java.util.List;

public class Skeleton extends Enemy {
    public Skeleton() {
        super(EnemyConstants.SKELETON);
    }

    @Override
    public List<ItemDrop> dropLoot() {
        return List.of();
    }
}

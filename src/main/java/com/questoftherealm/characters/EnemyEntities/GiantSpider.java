package com.questoftherealm.characters.EnemyEntities;

import com.questoftherealm.items.ItemDrop;

import java.util.List;

public class GiantSpider extends Enemy {
    public GiantSpider() {
        super(EnemyConstants.GIANT_SPIDER);
    }

    @Override
    public List<ItemDrop> dropLoot() {
        return List.of();
    }
}

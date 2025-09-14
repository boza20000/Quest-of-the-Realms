package com.questoftherealm.characters.EnemyEntities;

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

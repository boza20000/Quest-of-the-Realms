package com.questoftherealm.characters.EnemyEntities;

import com.questoftherealm.items.ItemDrop;

import java.util.List;

public class GoblinHorde extends Enemy {
    public GoblinHorde() {
        super(EnemyConstants.GOBLIN);
    }

    @Override
    public List<ItemDrop> dropLoot() {
        return List.of();
    }
}

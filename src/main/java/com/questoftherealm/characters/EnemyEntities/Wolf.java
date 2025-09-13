package com.questoftherealm.characters.EnemyEntities;

import com.questoftherealm.characters.EnemiesInterfaces.Fightable;
import com.questoftherealm.items.ItemDrop;

import java.util.List;

public class Wolf extends Enemy  {

    @Override
    public List<ItemDrop> dropLoot() {
        return List.of();
    }
}

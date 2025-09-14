package com.questoftherealm.characters.EnemyEntities;

import com.questoftherealm.characters.EnemiesInterfaces.Talkable;
import com.questoftherealm.items.ItemDrop;

import java.util.List;

public class GoblinGeneral extends Enemy implements Talkable {
    public GoblinGeneral() {
        super(EnemyConstants.GOBLIN_GENERAL);
    }

    @Override
    public List<ItemDrop> dropLoot() {
        return List.of();
    }

    @Override
    public void talk() {

    }
}

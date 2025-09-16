package com.questoftherealm.characters.EnemyEntities.entities;

import com.questoftherealm.characters.EnemiesInterfaces.Talkable;
import com.questoftherealm.characters.EnemyEntities.Enemy;
import com.questoftherealm.characters.EnemyEntities.EnemyConstants;
import com.questoftherealm.items.ItemDrop;

import java.util.List;

public class DarkMage extends Enemy implements Talkable {
    public DarkMage(){
        super(EnemyConstants.DARK_MAGE);
    }

    @Override
    public List<ItemDrop> dropLoot() {
        return List.of();
    }

    @Override
    public void talk() {

    }
}

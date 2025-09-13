package com.questoftherealm.characters.EnemyEntities;

import com.questoftherealm.characters.EnemiesInterfaces.Talkable;
import com.questoftherealm.items.ItemDrop;

import java.util.List;

public class Goblin extends Enemy implements Talkable {
    public Goblin(){

    }

    @Override
    public List<ItemDrop> dropLoot() {
        return List.of();
    }

    @Override
    public void talk() {

    }
}

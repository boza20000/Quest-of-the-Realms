package com.questoftherealm.enemyEntities.entities;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.enemyEntities.EnemiesInterfaces.Talkable;
import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.enemyEntities.EnemyConstants;
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
    public void talk(Player player) {

    }
}

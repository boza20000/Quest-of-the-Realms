package com.questoftherealm.enemyEntities.entities;

import com.questoftherealm.enemyEntities.EnemiesInterfaces.Talkable;
import com.questoftherealm.enemyEntities.EnemiesInterfaces.Tradeable;
import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.enemyEntities.EnemyConstants;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.items.Item;
import com.questoftherealm.items.ItemDrop;

import java.util.List;

public class TraderNPC extends Enemy implements Tradeable, Talkable {
    public TraderNPC(){
        super(EnemyConstants.TRAVELING_TRADER);
    }


    @Override
    public void trade(Player player, Item item, int amount) {

    }

    @Override
    public List<ItemDrop> dropLoot() {
        return List.of();
    }

    @Override
    public void talk(Player player) {

    }
}

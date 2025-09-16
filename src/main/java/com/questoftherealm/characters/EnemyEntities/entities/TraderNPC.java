package com.questoftherealm.characters.EnemyEntities.entities;

import com.questoftherealm.characters.EnemiesInterfaces.Talkable;
import com.questoftherealm.characters.EnemiesInterfaces.Tradeable;
import com.questoftherealm.characters.EnemyEntities.Enemy;
import com.questoftherealm.characters.EnemyEntities.EnemyConstants;
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
    public void talk() {

    }
}

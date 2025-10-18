package com.questoftherealm.characters.characterInterfaces;

import com.questoftherealm.enemyEntities.entities.TraderNPC;
import com.questoftherealm.items.Item;

public interface Trader {
    void buyItem(TraderNPC trader,Item item, int quantity);
    void sellItem(Item item,int quantity);
}
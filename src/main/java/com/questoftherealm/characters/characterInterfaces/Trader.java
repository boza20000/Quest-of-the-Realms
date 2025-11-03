package com.questoftherealm.characters.characterInterfaces;

import com.questoftherealm.enemyEntities.entities.SuspiciousTrader;
import com.questoftherealm.items.Item;

public interface Trader {
    void buyItem(SuspiciousTrader trader, Item item, int quantity);
    void sellItem(Item item,int quantity);
}
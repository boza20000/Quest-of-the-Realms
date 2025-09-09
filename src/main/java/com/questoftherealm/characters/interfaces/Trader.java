package com.questoftherealm.characters.interfaces;

import com.questoftherealm.items.Item;

public interface Trader {
    void buyItem(Item item,int quantity);
    void sellItem(Item item,int quantity);
}
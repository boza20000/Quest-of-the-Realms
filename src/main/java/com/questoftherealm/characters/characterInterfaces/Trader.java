package com.questoftherealm.characters.characterInterfaces;

import com.questoftherealm.items.Item;

public interface Trader {
    void buyItem(Item item,int quantity);
    void sellItem(Item item,int quantity);
}
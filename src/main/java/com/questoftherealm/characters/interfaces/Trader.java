package com.questoftherealm.characters.interfaces;

import com.questoftherealm.items.Item;

public interface Trader {
    void buyItem(Item item);
    void sellItem(Item item);
}
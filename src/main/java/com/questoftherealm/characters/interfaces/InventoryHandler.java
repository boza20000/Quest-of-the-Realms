package com.questoftherealm.characters.interfaces;

import com.questoftherealm.items.Item;

public interface InventoryHandler {
    void openInventory();
    void addItem(Item item,int quantity);
    void useItem(Item item);
    void equipItem(Item item);
}

package com.questoftherealm.characters.characterInterfaces;

import com.questoftherealm.items.Item;

public interface InventoryHandler {
    void openInventory();
    void useItem(Item item);
    void equipItem(Item item);
}

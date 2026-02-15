package com.questoftherealm.characters.characterInterfaces;

import com.questoftherealm.game.GameState;
import com.questoftherealm.items.Item;

public interface InventoryHandler {
    void openInventory(GameState state);
    void useItem(Item item);
    void equipItem(Item item,GameState state);
}

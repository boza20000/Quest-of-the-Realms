package com.questoftherealm.characters.player;

import com.questoftherealm.items.Item;
import com.questoftherealm.items.ItemRegistry;

public class WeaponFactory {
    private final ItemRegistry items;

    public WeaponFactory(ItemRegistry items) {
        this.items = items;
    }

    public Item create(String key) {
        return items.getItem(key);
    }
}


package com.questoftherealm.characters.EnemiesInterfaces;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.items.Item;

public interface Tradeable {
    void trade(Player player, Item item, int amount);
}

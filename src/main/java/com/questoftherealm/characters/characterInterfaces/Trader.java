package com.questoftherealm.characters.characterInterfaces;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.enemyEntities.entities.SuspiciousTrader;
import com.questoftherealm.game.GameState;
import com.questoftherealm.items.Item;

public interface Trader {
    void buyItem(SuspiciousTrader trader, Player player, Item item, int quantity,GameState state);
    void sellItem(Player player,SuspiciousTrader trader, Item item, int quantity,GameState state);
}
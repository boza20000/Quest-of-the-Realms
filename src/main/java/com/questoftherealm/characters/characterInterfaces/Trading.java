package com.questoftherealm.characters.characterInterfaces;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.game.GameState;
import com.questoftherealm.items.Item;
import com.questoftherealm.friendlyEntities.Entities.Trader;

public interface Trading {
    void buyItem(Trader trader, Player player, Item item, int quantity, GameState state);
}
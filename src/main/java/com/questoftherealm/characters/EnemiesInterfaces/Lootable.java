package com.questoftherealm.characters.EnemiesInterfaces;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.items.ItemDrop;

import java.util.List;

public interface Lootable {
    List<ItemDrop> dropLoot();
}

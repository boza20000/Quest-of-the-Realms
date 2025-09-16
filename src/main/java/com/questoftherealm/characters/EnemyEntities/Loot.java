package com.questoftherealm.characters.EnemyEntities;

import com.questoftherealm.items.Item;

public record Loot(Item item, double chance, int min, int max) {
}

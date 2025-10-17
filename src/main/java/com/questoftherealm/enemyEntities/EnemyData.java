package com.questoftherealm.enemyEntities;

import com.questoftherealm.items.Item;

import java.util.List;

public record EnemyData(String description, EnemyType type, int health, int baseAttack, int baseDefense,
                        List<Item> armor, Item weapon, List<Loot> loot, boolean isDead) {
}
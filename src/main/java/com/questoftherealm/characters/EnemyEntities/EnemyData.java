package com.questoftherealm.characters.EnemyEntities;

import com.questoftherealm.items.Item;

import java.util.List;

public record EnemyData(String description, EnemyType type, int health, List<Item> armor, Item weapon,boolean isDead) {
}
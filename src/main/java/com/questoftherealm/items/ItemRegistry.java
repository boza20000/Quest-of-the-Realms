package com.questoftherealm.items;

import java.util.List;

public class ItemRegistry {
    public static final List<Item> allItems = List.of(
            // ===== POTIONS =====
            new Item("Health Potion", ItemType.POTION, true, 20, 14, 3, Rarity.COMMON),
            new Item("Greater Health Potion", ItemType.POTION, true, 40, 25, 6, Rarity.UNCOMMON),
            new Item("Mana Potion", ItemType.POTION, true, 15, 12, 2, Rarity.COMMON),
            new Item("Greater Mana Potion", ItemType.POTION, true, 35, 20, 5, Rarity.UNCOMMON),
            new Item("Stamina Elixir", ItemType.POTION, true, 10, 8, 2, Rarity.COMMON),
            new Item("Elixir of Vigor", ItemType.POTION, true, 25, 18, 5, Rarity.UNCOMMON),

            // ===== WEAPONS =====
            new Item("Iron Sword", ItemType.WEAPON, false, 10, 20, 2, Rarity.COMMON),
            new Item("Steel Sword", ItemType.WEAPON, false, 15, 35, 3, Rarity.UNCOMMON),
            new Item("Orcish Axe", ItemType.WEAPON, false, 18, 40, 4, Rarity.RARE),
            new Item("Dagger of Shadows", ItemType.WEAPON, false, 12, 30, 2, Rarity.UNCOMMON),
            new Item("Mage’s Staff", ItemType.WEAPON, false, 8, 25, 3, Rarity.UNCOMMON),
            new Item("Elari Bow", ItemType.WEAPON, false, 14, 32, 3, Rarity.UNCOMMON),
            new Item("Flaming Sword", ItemType.WEAPON, false, 20, 50, 5, Rarity.RARE),
            new Item("Ice Dagger", ItemType.WEAPON, false, 15, 40, 4, Rarity.RARE),
            new Item("Silver Sword", ItemType.WEAPON, false, 18, 45, 4, Rarity.RARE),
            new Item("Crystal Staff", ItemType.WEAPON, false, 16, 40, 4, Rarity.RARE),
            new Item("Battle Axe", ItemType.WEAPON, false, 17, 48, 5, Rarity.RARE),

            // ===== ARMOR =====
            new Item("Leather Armor", ItemType.ARMOR, false, 5, 15, 1, Rarity.COMMON),
            new Item("Chainmail", ItemType.ARMOR, false, 10, 30, 2, Rarity.UNCOMMON),
            new Item("Plate Armor", ItemType.ARMOR, false, 15, 50, 3, Rarity.RARE),
            new Item("Orcish Helmet", ItemType.ARMOR, false, 8, 20, 1, Rarity.UNCOMMON),
            new Item("Elari Cloak", ItemType.ARMOR, false, 7, 18, 1, Rarity.UNCOMMON),
            new Item("Mage Robe", ItemType.ARMOR, false, 3, 12, 2, Rarity.COMMON),
            new Item("Boots of Swiftness", ItemType.ARMOR, false, 2, 15, 1, Rarity.COMMON),
            new Item("Gauntlets of Strength", ItemType.ARMOR, false, 5, 22, 2, Rarity.UNCOMMON),
            new Item("Dragon Scale Armor", ItemType.ARMOR, false, 20, 60, 4, Rarity.EPIC),
            new Item("Elari Hood", ItemType.ARMOR, false, 5, 18, 1, Rarity.UNCOMMON),
            new Item("Ring of Strength", ItemType.ARMOR, false, 5, 25, 2, Rarity.UNCOMMON),
            new Item("Amulet of Wisdom", ItemType.ARMOR, false, 5, 25, 2, Rarity.UNCOMMON),

            // ===== SCROLLS =====
            new Item("Scroll of Fireball", ItemType.SCROLL, false, 25, 20, 5, Rarity.UNCOMMON),
            new Item("Scroll of Ice Spike", ItemType.SCROLL, false, 20, 18, 4, Rarity.UNCOMMON),
            new Item("Scroll of Healing", ItemType.SCROLL, false, 15, 15, 3, Rarity.COMMON),
            new Item("Scroll of Protection", ItemType.SCROLL, false, 10, 12, 3, Rarity.COMMON),
            new Item("Scroll of Lightning", ItemType.SCROLL, false, 30, 25, 6, Rarity.RARE),

            // ===== CONSUMABLES =====
            new Item("Ration Pack", ItemType.CONSUMABLES, true, 5, 5, 1, Rarity.COMMON),
            new Item("Trail Mix", ItemType.CONSUMABLES, true, 3, 3, 1, Rarity.COMMON),
            new Item("Elari Fruit", ItemType.CONSUMABLES, true, 4, 4, 1, Rarity.COMMON),
            new Item("Honeyed Mead", ItemType.CONSUMABLES, true, 6, 8, 2, Rarity.COMMON),
            new Item("Dried Meat", ItemType.CONSUMABLES, true, 5, 6, 1, Rarity.COMMON),
            new Item("Spiced Jerky", ItemType.CONSUMABLES, true, 6, 7, 1, Rarity.COMMON),
            new Item("Sunfruit", ItemType.CONSUMABLES, true, 4, 5, 1, Rarity.COMMON),
            new Item("Moonberry Jam", ItemType.CONSUMABLES, true, 5, 6, 1, Rarity.UNCOMMON),
            new Item("Elari Nuts", ItemType.CONSUMABLES, true, 3, 4, 1, Rarity.COMMON),
            new Item("Mystic Tea", ItemType.CONSUMABLES, true, 2, 5, 2, Rarity.UNCOMMON),
            new Item("Herbal Tonic", ItemType.CONSUMABLES, true, 3, 6, 2, Rarity.UNCOMMON),
            new Item("Spiced Wine", ItemType.CONSUMABLES, true, 6, 8, 2, Rarity.UNCOMMON),
            new Item("Forest Berries", ItemType.CONSUMABLES, true, 4, 5, 1, Rarity.COMMON),
            new Item("Elixir of Focus", ItemType.CONSUMABLES, true, 0, 4, 3, Rarity.RARE),
            new Item("Glacial Water", ItemType.CONSUMABLES, true, 2, 3, 1, Rarity.COMMON),

            // ===== QUEST ITEMS =====
            new Item("Ancient Relic", ItemType.QUEST, false, 0, 50, 0, Rarity.EPIC),
            new Item("Orc Totem", ItemType.QUEST, false, 0, 40, 0, Rarity.EPIC),
            new Item("Elari Gemstone", ItemType.QUEST, false, 0, 45, 0, Rarity.EPIC),
            new Item("Mages’ Seal", ItemType.QUEST, false, 0, 35, 0, Rarity.EPIC),
            new Item("King’s Decree", ItemType.QUEST, false, 0, 30, 0, Rarity.EPIC),
            new Item("Dragon’s Eye", ItemType.QUEST, false, 0, 55, 0, Rarity.EPIC),
            new Item("Crown of Eldoria", ItemType.QUEST, false, 0, 60, 0, Rarity.EPIC),


            // ===== FRAGMENTS =====
            new Item("Fragment of Dawn", ItemType.FRAGMENT, true, 0, 25, 0, Rarity.UNCOMMON),
            new Item("Fragment of Twilight", ItemType.FRAGMENT, true, 0, 25, 0, Rarity.UNCOMMON),
            new Item("Fragment of the Arcane", ItemType.FRAGMENT, true, 0, 30, 0, Rarity.RARE),
            new Item("Shard of Valor", ItemType.FRAGMENT, true, 0, 28, 0, Rarity.RARE),
            new Item("Shard of Shadows", ItemType.FRAGMENT, true, 0, 28, 0, Rarity.RARE)
    );
}

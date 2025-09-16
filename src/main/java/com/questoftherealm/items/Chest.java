package com.questoftherealm.items;

import com.questoftherealm.exceptions.RandomItemNotGenerated;

import java.util.List;
import java.util.Random;

public class Chest {

    private final static Random random = new Random();

    public static ItemDrop generateRandomItem() {
        try {
            ItemType type = randomType();
            Rarity rarity = randomRarity();
            return randomItemFrom(type, rarity);
        } catch (Exception e) {
            throw new RandomItemNotGenerated("Generated Item not found");
        }
    }

    private static ItemType randomType() {
        ItemType[] category = {ItemType.ARMOR, ItemType.WEAPON, ItemType.POTION, ItemType.CONSUMABLES};
        return category[random.nextInt(category.length)];
    }

    private static int randomQuantity(Item item) {
        if (!item.isStackable()) return 1;

        switch (item.getRarity()) {
            case COMMON -> {
                return random.nextInt(1, 6);
            }
            case UNCOMMON -> {
                return random.nextInt(1, 4);
            }
            case RARE, EPIC -> {
                return random.nextInt(1, 3);
            }
            default -> {
                return 1;
            }
        }
    }

    private static Rarity randomRarity() {
        int rand = random.nextInt(100) + 1;
        if (rand <= 50) return Rarity.COMMON;
        if (rand <= 75) return Rarity.UNCOMMON;
        if (rand <= 90) return Rarity.RARE;
        if (rand <= 98) return Rarity.EPIC;
        return Rarity.LEGENDARY;
    }

    private static ItemDrop randomItemFrom(ItemType type, Rarity rarity) {
        List<Item> possibleItems = ItemRegistry.getAllItems().stream()
                .filter(i -> i.getRarity() == rarity && i.getType() == type)
                .toList();

        if (possibleItems.isEmpty()) {
            Rarity fallback = switch (rarity) {
                case LEGENDARY -> Rarity.EPIC;
                case EPIC -> Rarity.RARE;
                case RARE -> Rarity.UNCOMMON;
                default -> Rarity.COMMON;
            };
            possibleItems = ItemRegistry.getAllItems().stream()
                    .filter(i -> i.getType() == type && i.getRarity() == fallback)
                    .toList();
        }

        Item selectedItem = possibleItems.get(random.nextInt(possibleItems.size()));
        int quantity = randomQuantity(selectedItem);
        return new ItemDrop(selectedItem, quantity);
    }

}


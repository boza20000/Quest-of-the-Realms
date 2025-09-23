package com.questoftherealm.items;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.characters.playerCharacters.Mage;
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

    public static ItemDrop generateRandomWeapon(Player player) {

        ItemEffect effect = getWeaponType(player);
        ItemType type = ItemType.WEAPON;
        Rarity rarity = Rarity.COMMON;
        int quantity = 1;
        List<Item> possibleWeapons = ItemRegistry.getAllItems().stream()
                .filter(i -> i.getType() == type && i.getRarity() == rarity && i.getEffect() == effect)
                .toList();
        if (possibleWeapons.isEmpty()){
            throw new RandomItemNotGenerated("No weapons available for generation.");
        }
        Item weapon = possibleWeapons.get(random.nextInt(possibleWeapons.size()));
        return new ItemDrop(weapon, quantity);
    }

    private static ItemEffect getWeaponType(Player player) {
        return switch (player.getPlayerType()) {
            case Mage -> ItemEffect.STAFF;
            case Warrior -> ItemEffect.SWORD;
            case Rogue -> ItemEffect.DAGGER;
            case Orc -> ItemEffect.AXE;
        };
    }

    public static ItemDrop generateRandomHelmet(Player player) {
        ItemType type = ItemType.ARMOR;
        Rarity rarity = Rarity.COMMON;
        int quantity = 1;

        List<Item> possibleHelmets = ItemRegistry.getAllItems().stream()
                .filter(i -> i.getType() == type && i.getRarity() == rarity && i.getEffect() == ItemEffect.HELMET)
                .toList();

        if (possibleHelmets.isEmpty()) {
            throw new RandomItemNotGenerated("No helmets available for generation.");
        }

        Item helmet = possibleHelmets.get(random.nextInt(possibleHelmets.size()));
        return new ItemDrop(helmet, quantity);
    }

    public static ItemDrop generateRandomChestplate(Player player) {
        ItemType type = ItemType.ARMOR;
        Rarity rarity = Rarity.COMMON;
        int quantity = 1;

        List<Item> possibleChestplates = ItemRegistry.getAllItems().stream()
                .filter(i -> i.getType() == type && i.getRarity() == rarity && i.getEffect() == ItemEffect.CHESTPLATE)
                .toList();

        if (possibleChestplates.isEmpty()) {
            throw new RandomItemNotGenerated("No chestplates available for generation.");
        }

        Item chestplate = possibleChestplates.get(random.nextInt(possibleChestplates.size()));
        return new ItemDrop(chestplate, quantity);
    }

    public static ItemDrop generateRandomBoots(Player player) {
        ItemType type = ItemType.ARMOR;
        Rarity rarity = Rarity.COMMON;
        int quantity = 1;

        List<Item> possibleBoots = ItemRegistry.getAllItems().stream()
                .filter(i -> i.getType() == type && i.getRarity() == rarity && i.getEffect() == ItemEffect.BOOTS)
                .toList();

        if (possibleBoots.isEmpty()) {
            throw new RandomItemNotGenerated("No boots available for generation.");
        }

        Item boots = possibleBoots.get(random.nextInt(possibleBoots.size()));
        return new ItemDrop(boots, quantity);
    }
}


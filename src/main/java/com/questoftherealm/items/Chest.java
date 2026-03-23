package com.questoftherealm.items;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.exceptions.ArmorPieceNotGenerated;
import com.questoftherealm.exceptions.RandomItemNotGenerated;
import com.questoftherealm.exceptions.RandomWeaponNotGenerated;
import com.questoftherealm.game.GameState;

import java.util.List;
import java.util.Random;

public class Chest {
    private final GameState state;
    private final ItemRegistry itemRegistry;

    private Random random() {
        return state.getGameServices().getRandom().random();
    }

    public Chest(GameState state) {
        this.state = state;
        itemRegistry = state.getItemRegistry();
    }

    public ItemDrop generateRandomItem() {
        try {
            ItemType type = randomType();
            Rarity rarity = randomRarity();
            return randomItemFrom(type, rarity);
        } catch (Exception e) {
            throw new RandomItemNotGenerated(state.getMessages().getBundle().get("error.message.itemNotGenerated"));
        }
    }

    private ItemType randomType() {
        ItemType[] category = {ItemType.ARMOR, ItemType.WEAPON, ItemType.POTION, ItemType.CONSUMABLES};
        return category[random().nextInt(category.length)];
    }

    private int randomQuantity(Item item) {
        if (!item.isStackable()) return 1;

        switch (item.getRarity()) {
            case COMMON -> {
                return random().nextInt(1, 6);
            }
            case UNCOMMON -> {
                return random().nextInt(1, 4);
            }
            case RARE, EPIC -> {
                return random().nextInt(1, 3);
            }
            default -> {
                return 1;
            }
        }
    }

    private Rarity randomRarity() {
        int rand = random().nextInt(100) + 1;
        if (rand <= 50) return Rarity.COMMON;
        if (rand <= 75) return Rarity.UNCOMMON;
        if (rand <= 90) return Rarity.RARE;
        if (rand <= 98) return Rarity.EPIC;
        return Rarity.LEGENDARY;
    }

    private ItemDrop randomItemFrom(ItemType type, Rarity rarity) {
        List<Item> possibleItems = itemRegistry.getAllItems().stream()
                .filter(i -> i.getRarity() == rarity && i.getType() == type)
                .toList();

        if (possibleItems.isEmpty()) {
            Rarity fallback = switch (rarity) {
                case LEGENDARY -> Rarity.EPIC;
                case EPIC -> Rarity.RARE;
                case RARE -> Rarity.UNCOMMON;
                default -> Rarity.COMMON;
            };
            possibleItems = itemRegistry.getAllItems().stream()
                    .filter(i -> i.getType() == type && i.getRarity() == fallback)
                    .toList();
        }
        Item selectedItem = possibleItems.get(random().nextInt(possibleItems.size()));
        int quantity = randomQuantity(selectedItem);
        return new ItemDrop(selectedItem, quantity);
    }

    public ItemDrop generateRandomWeapon(Player player) {

        ItemEffect effect = getWeaponType(player);
        ItemType type = ItemType.WEAPON;
        Rarity rarity = Rarity.COMMON;
        int quantity = 1;
        List<Item> possibleWeapons = itemRegistry.getAllItems().stream()
                .filter(i -> i.getType() == type && i.getRarity() == rarity && i.getEffect() == effect)
                .toList();
        if (possibleWeapons.isEmpty()) {
            throw new RandomWeaponNotGenerated(state.getMessages().getBundle().get("error.message.weaponNotGenerated"));
        }
        Item weapon = possibleWeapons.get(random().nextInt(possibleWeapons.size()));
        return new ItemDrop(weapon, quantity);
    }

    private ItemEffect getWeaponType(Player player) {
        return switch (player.getPlayerType()) {
            case Mage -> ItemEffect.STAFF;
            case Warrior -> ItemEffect.SWORD;
            case Rogue -> ItemEffect.DAGGER;
            case Orc -> ItemEffect.AXE;
        };
    }

    public ItemDrop generateArmorPiece(ItemType type, ItemEffect effect) {

        List<Item> possibleArmor = itemRegistry.getAllItems().stream()
                .filter(i -> i.getType() == type && i.getRarity() == Rarity.COMMON && i.getEffect() == effect)
                .toList();
        if (possibleArmor.isEmpty()) {
            throw new ArmorPieceNotGenerated(state.getMessages().getBundle().get("error.message.armorPieceNotGenerated", effect));
        }

        return new ItemDrop(possibleArmor.get(random().nextInt(possibleArmor.size())),1);
    }



}


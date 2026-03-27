package com.questoftherealm.items;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.exceptions.ArmorPieceNotGenerated;
import com.questoftherealm.exceptions.RandomItemNotGenerated;
import com.questoftherealm.exceptions.RandomWeaponNotGenerated;
import com.questoftherealm.game.GameState;
import com.questoftherealm.server.ServerLogger;

import java.util.List;

import static com.questoftherealm.items.Rarity.COMMON;
import static com.questoftherealm.items.Rarity.UNCOMMON;
import static com.questoftherealm.items.Rarity.RARE;

public class Chest {
    private final GameState state;
    private final ItemRegistry itemRegistry;
    private static final int MAX_TRIES = 10;

    private int randomInt(int bound) {
        return state.getGameServices().getRandom().randomInt(bound);
    }

    private int randomInt(int start, int end) {
        return state.getGameServices().getRandom().randomInt(start, end);
    }

    public Chest(GameState state) {
        this.state = state;
        itemRegistry = state.getItemRegistry();
    }

    public ItemDrop generateRandomItem() {
        try {
            if (itemRegistry.getAllItems() == null || itemRegistry.getAllItems().isEmpty()) {
                ServerLogger.get().error("Chest: Item registry is empty or null - cannot generate items");
                throw new RandomItemNotGenerated(state.getMessages().getBundle().get("error.message.itemNotGenerated"));
            }
            
            ItemDrop drop = null;
            int count = 0;
            while (drop == null && count < MAX_TRIES) {
                ItemType type = randomType();
                Rarity rarity = randomRarity();
                drop = randomItemFrom(type, rarity);
                count++;
            }
            if (drop == null) {
                ServerLogger.get().warn("Chest: Failed to generate item after " + MAX_TRIES + " attempts");
                throw new RandomItemNotGenerated(state.getMessages().getBundle().get("error.message.itemNotGenerated"));
            }
            return drop;

        } catch (RuntimeException e) {
            ServerLogger.get().error("Chest: Unexpected error during item generation: " + e.getMessage(), e);
            throw new RandomItemNotGenerated(state.getMessages().getBundle().get("error.message.itemNotGenerated"));
        }
    }

    private ItemType randomType() {
        ItemType[] category = {ItemType.ARMOR, ItemType.WEAPON, ItemType.POTION, ItemType.CONSUMABLES};
        return category[randomInt(category.length)];
    }

    private int randomQuantity(Item item) {
        if (!item.isStackable()) return 1;

        switch (item.getRarity()) {
            case COMMON -> {
                return randomInt(1, 6);
            }
            case UNCOMMON -> {
                return randomInt(1, 4);
            }
            case RARE, EPIC -> {
                return randomInt(1, 3);
            }
            default -> {
                return 1;
            }
        }
    }

    private Rarity randomRarity() {
        int rand = randomInt(100) + 1;
        if (rand <= 50) return COMMON;
        if (rand <= 75) return UNCOMMON;
        if (rand <= 90) return RARE;
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
                case EPIC -> RARE;
                case RARE -> UNCOMMON;
                default -> COMMON;
            };
            possibleItems = itemRegistry.getAllItems().stream()
                    .filter(i -> i.getType() == type && i.getRarity() == fallback)
                    .toList();
        }
        if (possibleItems.isEmpty()) {
            return null;
        }
        Item selectedItem = possibleItems.get(randomInt(possibleItems.size()));
        int quantity = randomQuantity(selectedItem);
        return new ItemDrop(selectedItem, quantity);
    }

    public ItemDrop generateRandomWeapon(Player player) {

        ItemEffect effect = getWeaponType(player);
        ItemType type = ItemType.WEAPON;
        Rarity rarity = COMMON;
        int quantity = 1;
        List<Item> possibleWeapons = itemRegistry.getAllItems().stream()
                .filter(i -> i.getType() == type && i.getRarity() == rarity && i.getEffect() == effect)
                .toList();
        if (possibleWeapons.isEmpty()) {
            throw new RandomWeaponNotGenerated(state.getMessages().getBundle().get("error.message.weaponNotGenerated"));
        }
        Item weapon = possibleWeapons.get(randomInt(possibleWeapons.size()));
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
                .filter(i -> i.getType() == type && i.getRarity() == COMMON && i.getEffect() == effect)
                .toList();
        if (possibleArmor.isEmpty()) {
            throw new ArmorPieceNotGenerated(state.getMessages().getBundle().get("error.message.armorPieceNotGenerated", effect));
        }

        return new ItemDrop(possibleArmor.get(randomInt(possibleArmor.size())), 1);
    }


}

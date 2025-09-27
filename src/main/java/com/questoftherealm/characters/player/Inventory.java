package com.questoftherealm.characters.player;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.questoftherealm.game.GameConstants;
import com.questoftherealm.items.Item;
import com.questoftherealm.items.ItemKeyDeserializer;
import com.questoftherealm.items.ItemKeySerializer;

import java.util.HashMap;
import java.util.Map;

public class Inventory {
    @JsonSerialize(keyUsing = ItemKeySerializer.class)
    @JsonDeserialize(keyUsing = ItemKeyDeserializer.class)
    private final Map<Item, Integer> items = new HashMap<>();
    private final int capacity;

    public Inventory() {
        this.capacity = 20;
    }

    public Inventory(int capacity) {
        this.capacity = capacity;
    }

    public void addItem(Item item, int quantity) {
        if (item.isStackable()) {
            int curItemQuantity = items.getOrDefault(item, 0);
            int sum = curItemQuantity + quantity;
            if (sum <= GameConstants.MAX_ITEMS_IN_STACK) {
                items.put(item, sum);
                System.out.println(quantity + " x " + item + " added (now " + sum + ").");
            } else {
                System.out.println("Cannot carry more than " + GameConstants.MAX_ITEMS_IN_STACK + " of " + item.getName() + ".");
            }
        } else {
            if (items.size() < capacity) {
                items.put(item, items.getOrDefault(item, 0) + 1);
                System.out.println(item + " added in inventory.");
            } else {
                System.out.println("Inventory full! Cannot add " + item.getName());
            }
        }
    }

    public void removeItem(Item item, int quantity) {
        if (items.containsKey(item)) {
            int current = items.get(item);
            if (current <= quantity) {
                items.remove(item);
            } else {
                items.put(item, current - quantity);
            }
            System.out.println(quantity + " x " + item + " removed.");
        } else {
            System.out.println(item + " not found.");
        }
    }

    public void listItems() {
        if (items.isEmpty()) {
            System.out.println("Inventory is empty.");
        } else {
            System.out.println("=== Inventory ===");
            items.forEach((item, qty) -> System.out.println("- " + item + " x" + qty));
        }
    }

    public Map<Item, Integer> getItems() {
        return new HashMap<>(items) {
        }; // safe copy
    }

    public int getQuantity(Item item) {
        return items.getOrDefault(item, 0);
    }

    public boolean containsItem(Item item) {
        return getItems().containsKey(item);
    }
}

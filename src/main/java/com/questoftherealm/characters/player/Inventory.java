package com.questoftherealm.characters.player;

import com.questoftherealm.items.Item;

import java.util.HashMap;
import java.util.Map;

public class Inventory {
    private final Map<Item, Integer> items = new HashMap<>();
    private final int capacity;

    public Inventory(int capacity) {
        this.capacity = capacity;
    }

    public void addItem(Item item, int quantity) {
        if (capacity > items.size() && items.containsKey(item)) {
            items.put(item, items.getOrDefault(item, 0) + quantity);
            System.out.println(quantity + " x " + item + " added.");
        } else {
            System.out.println("Inventory full!");
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
}

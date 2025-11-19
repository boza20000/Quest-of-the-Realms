package com.questoftherealm.characters.player;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.questoftherealm.game.GameConstants;
import com.questoftherealm.game.GameState;
import com.questoftherealm.items.Item;
import com.questoftherealm.items.ItemKeyDeserializer;
import com.questoftherealm.items.ItemKeySerializer;
import com.questoftherealm.localization.MessageBundle;

import java.util.HashMap;
import java.util.Map;

public class Inventory {
    @JsonSerialize(keyUsing = ItemKeySerializer.class)
    @JsonDeserialize(keyUsing = ItemKeyDeserializer.class)
    private Map<Item, Integer> items = new HashMap<>();
    private final int capacity;

    public Inventory() {
        this.capacity = GameConstants.MAX_ITEMS_IN_STACK;
    }

    public Inventory(int capacity) {
        this.capacity = capacity;
    }

    public void addItem(Item item, int quantity,GameState state) {
        if (item.isStackable()) {
            int curItemQuantity = items.getOrDefault(item, 0);
            int sum = curItemQuantity + quantity;
            if (sum <= GameConstants.MAX_ITEMS_IN_STACK) {
                items.put(item, sum);
                state.getGameServices().getOutput().println(MessageBundle.get("inventory.added", quantity, item, sum));
            } else {
                state.getGameServices().getOutput().println(MessageBundle.get("inventory.cannotCarry",
                        GameConstants.MAX_ITEMS_IN_STACK, item.getName()));
            }
        } else {
            int curItemQuantity = items.getOrDefault(item, 0);
            int newTotal = curItemQuantity + quantity;

            if (items.size() < capacity || items.containsKey(item)) {
                items.put(item, newTotal);
                state.getGameServices().getOutput().println(MessageBundle.get("inventory.added", quantity, item, newTotal));
            } else {
                state.getGameServices().getOutput().println(MessageBundle.get("inventory.full", item.getName()));
            }
        }
    }

    public void removeItem(Item item, int quantity, GameState state) {
        if (items.containsKey(item)) {
            int current = items.get(item);
            if (current <= quantity) {
                items.remove(item);
            } else {
                items.put(item, current - quantity);
            }
            state.getGameServices().getOutput().println(MessageBundle.get("inventory.removed", quantity, item));
        } else {
            state.getGameServices().getOutput().println(MessageBundle.get("inventory.notFound", item));
        }
    }

    public void listItems(GameState state) {
        if (items.isEmpty()) {
            state.getGameServices().getOutput().println(MessageBundle.get("inventory.isEmpty"));
        } else {
            state.getGameServices().getOutput().println(MessageBundle.get("inventory.printStart"));
            items.forEach((item, qty) ->
                    state.getGameServices().getOutput().println(MessageBundle.get("inventory.listItems", item, qty)));
        }
    }

    public Map<Item, Integer> getItems() {
        return new HashMap<>(items);
    }

    public int getQuantity(Item item) {
        return items.getOrDefault(item, 0);
    }

    public boolean containsItem(Item item) {
        return items.containsKey(item);
    }

    public void clear() {
        items.clear();
    }
}

package com.questoftherealm.characters.player;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.questoftherealm.game.GameConstants;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.items.Item;
import com.questoftherealm.items.ItemKeyDeserializer;
import com.questoftherealm.items.ItemKeySerializer;
import com.questoftherealm.localization.MessageBundle;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class Inventory {
    @JsonSerialize(keyUsing = ItemKeySerializer.class)
    @JsonDeserialize(keyUsing = ItemKeyDeserializer.class)
    private Map<Item, Integer> items = new ConcurrentHashMap<>();
    private final int capacity;

    public Inventory() {
        this.capacity = GameConstants.MAX_ITEMS_IN_STACK;
    }

    public Inventory(int capacity) {
        this.capacity = capacity;
    }

    public synchronized void addItem(Item item, int quantity, GameState state) {
        if (item.isStackable()) {
            handleStackableItems(item, quantity, state.getGameServices().getOutput(), state.getMessages().getBundle());
        } else {
            handleNotStackableItems(item, quantity, state.getGameServices().getOutput(), state.getMessages().getBundle());
        }
    }

    public synchronized void removeItem(Item item, int quantity, GameState state) {
        if (items.containsKey(item)) {
            int current = items.get(item);
            if (current <= quantity) {
                items.remove(item);
            } else {
                items.put(item, current - quantity);
            }
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("inventory.removed", quantity, item));
        } else {
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("inventory.notFound", item));
        }
    }

    public synchronized void listItems(Output output, MessageBundle bundle) {
        if (items.isEmpty()) {
            output.println(bundle.get("inventory.isEmpty"));
        } else {
            output.println(bundle.get("inventory.printStart"));
            items.forEach((item, qty) ->
                    output.println(bundle.get("inventory.listItems", item, qty)));
        }
    }

    public synchronized Map<Item, Integer> getItems() {
        return new HashMap<>(items);
    }

    public synchronized int getQuantity(Item item) {
        return items.getOrDefault(item, 0);
    }

    public synchronized boolean containsItem(Item item) {
        return items.containsKey(item);
    }


    public synchronized void clear() {
        items.clear();
    }

    private  void handleStackableItems(Item item, int quantity, Output output, MessageBundle bundle) {
        int curItemQuantity = items.getOrDefault(item, 0);
        int sum = curItemQuantity + quantity;
        if (sum <= GameConstants.MAX_ITEMS_IN_STACK) {
            items.put(item, sum);
            output.println(bundle.get("inventory.added", quantity, item, sum));
        } else {
            output.println(bundle.get("inventory.cannotCarry",
                    GameConstants.MAX_ITEMS_IN_STACK, item.getName()));
        }
    }

    private  void handleNotStackableItems(Item item, int quantity, Output output, MessageBundle bundle) {
        int curItemQuantity = items.getOrDefault(item, 0);
        int newTotal = curItemQuantity + quantity;

        if (items.size() < capacity || items.containsKey(item)) {
            items.put(item, newTotal);
            output.println(bundle.get("inventory.added", quantity, item, newTotal));
        } else {
            output.println(bundle.get("inventory.full", item.getName()));
        }
    }

}

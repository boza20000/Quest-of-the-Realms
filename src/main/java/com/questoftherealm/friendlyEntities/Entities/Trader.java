package com.questoftherealm.friendlyEntities.Entities;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.playerCharacters.Warrior;
import com.questoftherealm.friendlyEntities.Npc;
import com.questoftherealm.friendlyEntities.NpcType;
import com.questoftherealm.game.GameConstants;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.interaction.MissionInteractions;
import com.questoftherealm.server.ServerLogger;
import com.questoftherealm.exceptions.ItemNotFound;
import com.questoftherealm.items.Chest;
import com.questoftherealm.items.Item;
import com.questoftherealm.items.ItemDrop;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Trader extends Npc {
    private final Map<Item, Integer> itemsForSale;
    private final GameState state;
    private boolean isTrading;

    public Trader(String id, GameState state, MissionInteractions missionInteractions) {
        super(NpcType.TRADER, id, state, missionInteractions);
        this.state = state;
        itemsForSale = generateItemsForSale();
    }

    private Output output() {
        return state.getGameServices().getOutput();
    }

    @Override
    public void talk(GameState state, Player player, boolean isSimulation) {
        isTrading = true;
        if (!(player.getPlayerCharacter() instanceof Warrior)) {
            output().println(state.getMessages().getBundle().get("trader.refusesToTalk"));
            return;
        }
        output().println(state.getMessages().getBundle().get("trader.talks"));
        showItemsForSale();
        output().println(state.getMessages().getBundle().get("trader.talks.how.to.buy"));
        while (isTrading) {
            handleTrading(player);
        }
    }

    private synchronized void handleTrading(Player player) {
        Warrior warrior = (Warrior) player.getPlayerCharacter();
        String input = state.getGameServices().getInput().nextLine();
        
        if (input == null || input.isBlank()) {
            output().println(state.getMessages().getBundle().get("trader.talks.invalid.buy.command"));
            return;
        }
        
        if (input.equalsIgnoreCase("stop")) {
            isTrading = false;
            output().println(state.getMessages().getBundle().get("trader.talks.goodbye", player.getName()));
            return;
        }
        
        String[] parts = input.split(" ");
        if (!validatePurchaseCommand(parts)) {
            return;
        }
        
        PurchaseDetails details = parseItemAndQuantity(parts);
        if (details == null) {
            return;
        }
        
        processPurchase(player, warrior, details.itemName(), details.quantity());
    }

    private boolean validatePurchaseCommand(String[] parts) {
        if (parts.length < 3 || !parts[0].equalsIgnoreCase("buy")) {
            output().println(state.getMessages().getBundle().get("trader.talks.invalid.buy.command"));
            return false;
        }
        return true;
    }

    private PurchaseDetails parseItemAndQuantity(String[] parts) {
        String itemName = String.join(" ", Arrays.copyOfRange(parts, 1, parts.length - 1));
        
        int quantity;
        try {
            quantity = Integer.parseInt(parts[parts.length - 1]);
        } catch (NumberFormatException e) {
            ServerLogger.get().warn("Trader: Invalid quantity input - " + parts[parts.length - 1], e);
            output().println(state.getMessages().getBundle().get("trader.talks.invalid.buy.command"));
            return null;
        }
        
        return new PurchaseDetails(itemName, quantity);
    }

    private void processPurchase(Player player, Warrior warrior, String itemName, int quantity) {
        Item searchItem;
        try {
            searchItem = state.getItemRegistry().getItem(itemName);
        } catch (ItemNotFound e) {
            ServerLogger.get().info("Trader: Item not found - " + itemName);
            output().println(state.getMessages().getBundle().get("trader.talks.item.not.for.sale", itemName));
            return;
        }

        if (!itemsForSale.containsKey(searchItem)) {
            output().println(state.getMessages().getBundle().get("trader.talks.item.not.for.sale", itemName));
            return;
        }

        if (!hasEnoughStock(searchItem, quantity)) {
            output().println(state.getMessages().getBundle().get("trader.talks.not.enough.stock", itemName));
            return;
        }

        updateStock(searchItem, quantity);
        warrior.buyItem(this, player, searchItem, quantity, state);
    }

    private boolean hasEnoughStock(Item item, int requestedQuantity) {
        int currentQuantity = itemsForSale.get(item);
        return requestedQuantity <= currentQuantity;
    }

    private void updateStock(Item item, int quantitySold) {
        itemsForSale.put(item, itemsForSale.get(item) - quantitySold);
    }

    synchronized void showItemsForSale() {
        output().println(state.getMessages().getBundle().get("trader.itemsForSale"));
        for (Item item : itemsForSale.keySet()) {
            output().println(state.getMessages().getBundle().get("trader.itemListing", item.getName(), itemsForSale.get(item), item.getPrice()));
        }
    }

    private Map<Item, Integer> generateItemsForSale() {
        Map<Item, Integer> items = new HashMap<>();
        while (items.size() < GameConstants.TRADER_MAX_ITEMS) {
            Chest chest = new Chest(state);
            ItemDrop item = chest.generateRandomItem();
            items.put(item.item(), item.quantity());
        }
        return items;
    }
}

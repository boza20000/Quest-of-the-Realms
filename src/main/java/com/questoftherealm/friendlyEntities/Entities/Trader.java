package com.questoftherealm.friendlyEntities.Entities;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.playerCharacters.Warrior;
import com.questoftherealm.friendlyEntities.Npc;
import com.questoftherealm.friendlyEntities.NpcType;
import com.questoftherealm.game.GameConstants;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.interaction.MissionInteractions;
import com.questoftherealm.items.Chest;
import com.questoftherealm.items.Item;
import com.questoftherealm.items.ItemDrop;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Trader extends Npc {
    private Map<Item, Integer> itemsForSale;
    private GameState state;
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
        if (input == null) {
            output().println(state.getMessages().getBundle().get("trader.talks.invalid.buy.command"));
            return;
        }
        if (input.equalsIgnoreCase("stop")) {
            isTrading = false;
            output().println(state.getMessages().getBundle().get("trader.talks.goodbye", player.getName()));
            return;
        }
        String[] parts = input.split(" ");
        if (parts.length < 3 || !parts[0].equalsIgnoreCase("buy")) {
            output().println(state.getMessages().getBundle().get("trader.talks.invalid.buy.command"));
            return;
        }
        String itemName = String.join(" ", Arrays.copyOfRange(parts, 1, parts.length - 1));

        int quantity;
        try {
            quantity = Integer.parseInt(Arrays.stream(parts).toList().getLast());
        } catch (NumberFormatException e) {
            output().println(state.getMessages().getBundle().get("trader.talks.invalid.buy.command"));
            return;
        }
        Item serachItem = state.getItemRegistry().getItem(itemName);

        if(serachItem!=null && !itemsForSale.containsKey(serachItem)){
            output().println(state.getMessages().getBundle().get("trader.talks.item.not.for.sale", itemName));
            return;
        }

        int currentQuantity = itemsForSale.get(serachItem);
        if(quantity<=currentQuantity){
            itemsForSale.put(serachItem, currentQuantity - quantity);
        } else {
            output().println(state.getMessages().getBundle().get("trader.talks.not.enough.stock", itemName));
            return;
        }

        warrior.buyItem(this, player, serachItem, quantity, state);
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

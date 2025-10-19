package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.exceptions.ItemNotFound;
import com.questoftherealm.game.Game;
import com.questoftherealm.items.Item;

import java.util.Arrays;

import static com.questoftherealm.items.ItemRegistry.getItem;


public class UseCommand extends Command {

    public UseCommand() {
        super("use");
    }

    @Override
    public String getDescription() {
        return "use [item name] — uses an item from your inventory (e.g., potion, scroll, etc.)";
    }

    @Override
    public boolean makeSafe(String[] args, Player player) {
        if (args.length < 2) {
            System.out.println("Usage: " + getDescription());
            return false;
        }
        return playerBaseCheck(player);
    }

    @Override
    public void execute(String[] args) {
        Player player = Game.getPlayer();
        if (!makeSafe(args, player)) {
            return;
        }
        String nameItem = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        Item item;
        try {
            item = getItem(nameItem);
        } catch (IllegalArgumentException e) {
            System.out.println("This is not item");
            return;
        } catch (ItemNotFound ex) {
            System.out.println("Item not found");
            return;
        }
        if (player.getInventory().containsItem(item)) {
            player.useItem(item);
            player.getInventory().removeItem(item, 1);
        }
    }
}

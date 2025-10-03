package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.game.Game;
import com.questoftherealm.items.Item;
import com.questoftherealm.items.ItemDrop;
import com.questoftherealm.map.Map;
import com.questoftherealm.map.Tile;

import static com.questoftherealm.items.ItemRegistry.getItem;

public class TakeCommand extends Command {
    public TakeCommand() {
        super("take");
    }

    @Override
    public void execute(String[] args) {
        String input = String.join(" ", args);
        String command = args[0];
        String rest = input.substring(command.length()).trim();
        int lastSpace = rest.lastIndexOf(" ");
        if (lastSpace == -1) {
            System.out.println("You must specify a quantity.");
            return;
        }
        String itemName = rest.substring(0, lastSpace).trim();
        int quantity;
        try {
            quantity = Integer.parseInt(rest.substring(lastSpace + 1));
        } catch (NumberFormatException e) {
            System.out.println("Quantity must be a number.");
            return;
        }
        Item newItem = getItem(itemName);
        Player player = Game.getPlayer();
        Map map = Game.getGameMap();
        Tile curZone = map.curZone(player.getX(), player.getY());

        ItemDrop drop = curZone.getDrops().stream()
                .filter(d -> d.item().equals(newItem))
                .findFirst()
                .orElse(null);

        if (drop != null && drop.quantity() >= quantity) {
            player.getInventory().addItem(newItem, quantity);
            curZone.removeDrop(newItem, quantity);
            System.out.println("You picked up " + quantity + "x " + newItem.getName());
        } else {
            System.out.println("No such item or not enough quantity.");
        }
    }

    @Override
    public String getDescription() {
        return "the player takes the item from the ground and puts it in his inventory(take [itemName] [quantity])";
    }
}

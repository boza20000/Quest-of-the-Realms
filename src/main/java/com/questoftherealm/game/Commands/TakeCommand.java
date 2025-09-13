package com.questoftherealm.game.Commands;

import com.questoftherealm.game.Game;
import com.questoftherealm.items.Item;

import static com.questoftherealm.items.ItemRegistry.getItem;

public class TakeCommand extends Command {
    public TakeCommand() {
        super("take");
    }

    @Override
    public void execute(String[] args) {
        String itemName = args[1];
        Item newItem = getItem(itemName);
        int quantity = Integer.parseInt(args[2]);
        if (Game.getGameMap().curZone(Game.getPlayer().getX(), Game.getPlayer().getY()).pickItem(itemName).item() == newItem
                && Game.getGameMap().curZone(Game.getPlayer().getX(), Game.getPlayer().getY()).pickItem(itemName).quantity() <= quantity) {
            Game.getPlayer().getInventory().addItem(newItem, quantity);
        } else {
            System.out.println("No such item");
        }
    }

    @Override
    public String getDescription() {
        return "the player takes the item from the ground and puts it in his inventory(take [itemName] [quantity])";
    }
}

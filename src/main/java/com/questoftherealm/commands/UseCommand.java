package com.questoftherealm.commands;

import com.questoftherealm.game.Game;
import com.questoftherealm.items.Item;

import static com.questoftherealm.items.ItemRegistry.getItem;


public class UseCommand extends Command {

    public UseCommand() {
        super("use");
    }

    @Override
    public String getDescription() {
        return "use [itemName]";
    }

    @Override
    public void execute(String[] args) {
        String nameItem = args[1];
        Item item = getItem(nameItem);
        if(Game.getPlayer().getInventory().containsItem(item)) {
            Game.getPlayer().useItem(item);
            Game.getPlayer().getInventory().removeItem(item,1);
        }
    }
}

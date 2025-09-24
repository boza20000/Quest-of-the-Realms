package com.questoftherealm.commands;

import com.questoftherealm.game.Game;
import com.questoftherealm.items.Item;

import static com.questoftherealm.items.ItemRegistry.getItem;

public class EquipCommand extends Command {
    public EquipCommand() {
        super("equip");
    }

    @Override
    public String getDescription() {
        return "equip [itemName]";
    }

    @Override
    public void execute(String[] args) {
        String itemName = args[1];
        Item item = getItem(itemName);
        if(Game.getPlayer().getInventory().containsItem(item)) {
            Game.getPlayer().equipItem(item);
            System.out.print(item.getName() + " equipped successfully! ");
        }
        else{
            System.out.println("Item not in inventory");
        }
    }
}

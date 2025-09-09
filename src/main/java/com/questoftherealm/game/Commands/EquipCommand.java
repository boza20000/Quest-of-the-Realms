package com.questoftherealm.game.Commands;

import com.questoftherealm.game.Game;
import com.questoftherealm.items.Item;

import static com.questoftherealm.items.ItemRegistry.getItem;

public class EquipCommand extends Command {
    public EquipCommand() {
        super("equip");
    }

    @Override
    public String getDescription() {
        return "equip [item]";
    }

    @Override
    public void execute(String[] args) {
        String itemName = args[1];
        Item item  = getItem(itemName);
        switch (item.getType()){
            case ARMOR ->  Game.getPlayer().addArmorPiece(item);
            case WEAPON -> Game.getPlayer().equipWeapon(item);
        }


    }
}

package com.questoftherealm.expeditions.missions;

import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.game.Game;
import com.questoftherealm.items.Item;
import com.questoftherealm.items.ItemType;
import java.util.Map;

public final class Gather_Supplies extends Mission {
    public Gather_Supplies() {
        super("Gather Supplies", "Collect at least 1 potion and 5 food for the journey ahead.");
    }

    @Override
    public boolean checkCompletion() {
        if(isCompleted())return true;
        boolean hasPotion = false;
        int foodSum = 0;
        Map<Item, Integer> inventory = Game.getPlayer().getInventory().getItems();
        for (Item i : inventory.keySet()) {
            int quantity = inventory.get(i);
            if (i.getType() == ItemType.POTION && quantity >= 1) {
                hasPotion = true;
            }
            if (i.getType() == ItemType.CONSUMABLES && quantity >= 1) {
                foodSum += quantity;
            }
        }
        if (hasPotion && foodSum >= 5) {
            complete();
            return true;
        }
        return false;
    }
}

package com.questoftherealm.expeditions.missions;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.expeditions.quests.StartQuest;
import com.questoftherealm.items.Item;
import com.questoftherealm.items.ItemType;
import java.util.Map;

public class Gather_Supplies extends Mission {
    public Gather_Supplies(Player player) {
        super("Gather Supplies",
                "Collect at least 1 potion and 5 food for the journey ahead.",
                player,
                (p, m) -> {
                    boolean hasPotion = false;
                    int foodSum = 0;
                    if (p == null || p.getInventory() == null) {
                        return false;
                    }
                    Map<Item, Integer> inventory = p.getInventory().getItems();
                    for (Item i : inventory.keySet()) {
                        int quantity = inventory.get(i);
                        if (i.getType() == ItemType.POTION && quantity >= 1) {
                            hasPotion = true;
                        }
                        if (i.getType() == ItemType.CONSUMABLES && quantity >= 1) {
                            foodSum += quantity;
                        }
                    }
                    return (p.getCurQuest() instanceof StartQuest && hasPotion && foodSum >= 5);
                }
        );
    }


}

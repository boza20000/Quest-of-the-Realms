package com.questoftherealm.characters.playerCharacters;

import com.questoftherealm.characters.interfaces.Trader;
import com.questoftherealm.game.Game;
import com.questoftherealm.items.Item;
import com.questoftherealm.items.ItemRegistry;

public class Warrior extends Characters implements Trader {

    public Warrior() {
        super(45, 5, 6, 4, 12, 5, 3, 4);
    }

    public Warrior(int health, int mana, int attack, int defence, int armor, int charisma, int spells, int intelligence) {
        super(health, mana, attack, defence, armor, charisma, spells, intelligence);
    }

    @Override
    public void buyItem(Item item, int quantity) {

    }

    @Override
    public void sellItem(Item item, int quantity) {
        int money = item.getPrice();
        Game.getPlayer().addMoney(money);
        Game.getPlayer().getInventory().removeItem(item, quantity);
    }

    @Override
    public Item getDefaultWeapon() {
        return ItemRegistry.getItem("Wooden Staff");
    }

}

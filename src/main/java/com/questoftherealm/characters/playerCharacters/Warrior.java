package com.questoftherealm.characters.playerCharacters;

import com.questoftherealm.characters.characterInterfaces.Trader;
import com.questoftherealm.game.Game;
import com.questoftherealm.items.Item;
import com.questoftherealm.items.ItemRegistry;
import static com.questoftherealm.characters.playerCharacters.CharacterConstants.*;

public class Warrior extends Characters implements Trader {

    public Warrior() {
        super(WARRIOR_HEALTH,
                WARRIOR_MANA,
                WARRIOR_ATTACK,
                WARRIOR_DEFENCE,
                WARRIOR_ARMOR,
                WARRIOR_CHARISMA,
                WARRIOR_SPELLS,
                WARRIOR_INTELLIGENCE);
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
        return ItemRegistry.getItem("Bronze Sword");
    }

    @Override
    public int getBaseAttack() {
        return ROGUE_ATTACK;
    }

    @Override
    public int getBaseDefence() {
        return ROGUE_DEFENCE;
    }

}

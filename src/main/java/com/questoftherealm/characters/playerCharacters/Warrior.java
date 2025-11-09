package com.questoftherealm.characters.playerCharacters;

import com.questoftherealm.characters.characterInterfaces.Trader;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.enemyEntities.entities.SuspiciousTrader;
import com.questoftherealm.game.Game;
import com.questoftherealm.items.Item;
import com.questoftherealm.items.ItemRegistry;
import com.questoftherealm.localization.MessageBundle;
import static com.questoftherealm.characters.playerCharacters.CharacterConstants.*;

public class Warrior extends Characters implements Trader {

    public Warrior() {
        super(WARRIOR_HEALTH, WARRIOR_MANA, WARRIOR_ATTACK, WARRIOR_DEFENCE, WARRIOR_ARMOR, WARRIOR_CHARISMA, WARRIOR_SPELLS, WARRIOR_INTELLIGENCE);
    }

    public Warrior(int health, int mana, int attack, int defence, int armor, int charisma, int spells, int intelligence) {
        super(health, mana, attack, defence, armor, charisma, spells, intelligence);
    }

    @Override
    public void buyItem(SuspiciousTrader trader, Item item, int quantity) {
        // Implemented elsewhere
    }

    @Override
    public void sellItem(Item item, int quantity) {
        int money = item.getPrice();
        Game.getPlayer().addMoney(money);
        Game.getPlayer().getInventory().removeItem(item, quantity);
    }

    @Override
    public Item getDefaultWeapon() {
        return ItemRegistry.getItem(MessageBundle.get("warrior.weapon.default"));
    }

    @Override
    public int getBaseAttack() {
        return WARRIOR_ATTACK;
    }

    @Override
    public int getBaseDefence() {
        return WARRIOR_DEFENCE;
    }

    @Override
    public int getMaxHealth() {
        return WARRIOR_HEALTH;
    }

    @Override
    public void activateAbility(Player player, Enemy enemy) {
        System.out.println(MessageBundle.get("warrior.ability.use", player.getWeapon().getName()));
        enemy.takeDamage(player.getPlayerCharacter().getAttack() * 2);
    }
}

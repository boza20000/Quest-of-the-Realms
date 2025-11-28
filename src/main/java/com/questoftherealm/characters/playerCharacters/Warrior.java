package com.questoftherealm.characters.playerCharacters;

import com.questoftherealm.characters.characterInterfaces.Trader;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.enemyEntities.entities.SuspiciousTrader;

import com.questoftherealm.game.GameState;
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
    public void buyItem(SuspiciousTrader trader, Player player, Item item, int quantity, GameState state) {
        // Implemented elsewhere
    }

    @Override
    public void sellItem(Player player, SuspiciousTrader trader, Item item, int quantity, GameState state) {
        int money = item.getPrice();
        player.addMoney(money,state);
        player.getInventory().removeItem(item, quantity, state);
    }

    @Override
    public String getDefaultWeapon(GameState state) {
        return state.getMessages().getBundle().get("warrior.weapon.default");
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
    public void activateAbility(Player player, Enemy enemy, GameState state) {
        state.getGameServices().getOutput().println(state.getMessages().getBundle().get("warrior.ability.use", player.getWeapon().getName()));
        enemy.takeDamage(player.getPlayerCharacter().getAttack() * 2, state);
    }
}

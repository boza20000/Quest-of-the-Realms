package com.questoftherealm.characters.playerCharacters;

import com.questoftherealm.characters.characterInterfaces.Trading;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.enemyEntities.Enemy;

import com.questoftherealm.friendlyEntities.Entities.Trader;
import com.questoftherealm.game.GameConstants;
import com.questoftherealm.game.GameState;
import com.questoftherealm.items.Item;

import static com.questoftherealm.characters.playerCharacters.CharacterConstants.*;

public class Warrior extends Characters implements Trading {

    public Warrior() {
        super(WARRIOR_HEALTH, WARRIOR_MANA, WARRIOR_ATTACK, WARRIOR_DEFENCE, WARRIOR_ARMOR, WARRIOR_CHARISMA, WARRIOR_SPELLS, WARRIOR_INTELLIGENCE);
    }

    public Warrior(int health, int mana, int attack, int defence, int armor, int charisma, int spells, int intelligence) {
        super(health, mana, attack, defence, armor, charisma, spells, intelligence);
    }

    @Override
    public void buyItem(Trader trader, Player player, Item item, int quantity, GameState state) {
        if (player.getInventory().getItems().size() == GameConstants.MAX_ITEMS_IN_INVENTORY) {
            if (hasSpace(player, item, quantity)) {
                state.getGameServices().getOutput().println("Not enough space in inventory");
                return;
            }
        }

        if (!(player.getGold() < (item.getPrice() * quantity))) {
            state.getGameServices().getOutput().println("Not enough money");
            return;
        }

        player.payMoney(item.getPrice() * quantity, state);
        player.getInventory().addItem(item, quantity, state);
    }

    private boolean hasSpace(Player player, Item item, int quantity) {
        if (item.isStackable()) {
            return player.getInventory().getItems().get(item) + quantity <= GameConstants.MAX_ITEMS_IN_STACK;
        } else {
            return false;
        }
    }

    @Override
    public void sellItem(Player player, Trader trader, Item item, int quantity, GameState state) {
        player.addMoney(item.getPrice() * quantity, state);
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

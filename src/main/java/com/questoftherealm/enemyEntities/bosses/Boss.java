package com.questoftherealm.enemyEntities.bosses;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.playerCharacters.Characters;
import com.questoftherealm.game.GameState;
import com.questoftherealm.items.Item;
import com.questoftherealm.items.ItemDrop;
import com.questoftherealm.items.ItemEffect;

import java.util.HashMap;
import java.util.List;

public abstract class Boss extends Characters {
    protected String name;
    protected HashMap<ItemEffect, Item> armor;
    protected Item weapon;
    protected List<ItemDrop> loot;
    private boolean isDefeated;


    public Boss(
            int health, int mana, int attack, int defence, int armorValue,
            int charisma, int spells, int intelligence, String name,
            HashMap<ItemEffect, Item> armor, Item weapon, List<ItemDrop> loot, boolean isDefeated) {

        super(health, mana, attack, defence, armorValue, charisma, spells, intelligence);
        this.name = name;
        this.armor = armor;
        this.weapon = weapon;
        this.loot = loot;
        this.isDefeated = false;
    }

    public boolean isDefeated() {
        return isDefeated;
    }

    public void setDefeated(boolean defeated) {
        isDefeated = defeated;
    }

    public abstract void superMove(Player player, GameState state);


    public synchronized void takeDamage(int damageToBoss, GameState state) {
        if (isDefeated()) {
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("boss.already.defeated", this.name));
            return;
        }
        if (damageToBoss > 0) {
            setHealth(Math.max(getHealth() - damageToBoss, 0));
            state.getGameServices().getOutput().println("💥 "
                    + state.getMessages().getBundle().get("boss.take.damage", this.getClass().getSimpleName(), damageToBoss));
        }
        if (getHealth() == 0) {
            setDefeated(true);
            state.getGameServices().getOutput().println("🏆 "
                    + state.getMessages().getBundle().get("boss.defeated", this.name, this.getClass().getSimpleName()));
        }
    }

}

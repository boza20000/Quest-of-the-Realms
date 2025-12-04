package com.questoftherealm.enemyEntities.bosses;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.playerCharacters.Characters;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.items.Item;
import com.questoftherealm.items.ItemDrop;
import com.questoftherealm.items.ItemEffect;
import com.questoftherealm.localization.MessageBundle;

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

    @Override
    public void takeDamage(int damageToBoss, GameState state) {
        if (damageToBoss > 0) {
            setHealth(Math.max(getHealth() - damageToBoss, 0));
            state.getGameServices().getOutput().println("💥 "
                    + state.getMessages().getBundle().get("boss.take.damage", this.getClass().getSimpleName(),damageToBoss));
        }
    }

}

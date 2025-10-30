package com.questoftherealm.spells;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.enemyEntities.Enemy;

public abstract class Spell {
    private final String spellName;
    private final int power;
    private final String description;
    private final int manaCost;

    public Spell(String spellName, int power, String description, int manaCost) {
        this.power = power;
        this.spellName = spellName;
        this.description = description;
        this.manaCost = manaCost;
    }

    public int takePower() {
        return power;
    }

    public int getManaCost() {
        return manaCost;
    }

    public String getSpellName() {
        return spellName;
    }

    public String getDescription() {
        return description;
    }

    public abstract void cast(Player player, Enemy enemy);

    public abstract String getSymbol();
}

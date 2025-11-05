package com.questoftherealm.spells;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.enemyEntities.Enemy;

public abstract class DamageSpell extends Spell {
    public DamageSpell(String spellName, int power, String description, int manaCost) {
        super(spellName, power, description, manaCost);
    }

    protected void damage(Player player, Enemy enemy, Spell spell) {
        if (player.getPlayerCharacter().getMana() < spell.getManaCost()) {
            System.out.println("💤 Not enough mana to cast " + spell.getSpellName() + "!");
            return;
        }
        if (!player.getPlayerType().equals(PlayerTypes.Mage)) {
            System.out.println("🚫 This class cannot cast spells.");
        }
        enemy.takeDamage(spell.takePower());
        player.loseMana(spell.getManaCost());
        System.out.println(spell.getSymbol() + " You cast " + spell.getSpellName() + "!");
        System.out.println("💥 " + spell.getDescription());

    }
}

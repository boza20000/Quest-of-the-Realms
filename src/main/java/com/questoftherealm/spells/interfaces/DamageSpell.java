package com.questoftherealm.spells.interfaces;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.spells.Spell;

public interface DamageSpell {
    default void damage(Player player, Enemy enemy,Spell spell) {
        if (player.getPlayerType().equals(PlayerTypes.Mage)) {
            enemy.takeDamage(spell.takePower());
            player.loseMana(spell.getManaCost());
            System.out.println(spell.getSymbol() + " You cast " + spell.getSpellName() + "!");
            System.out.println("💥 " + spell.getDescription());

        } else {
            System.out.println("🚫 This class cannot cast spells.");
        }
    }
}

package com.questoftherealm.spells;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.localization.MessageBundle;

public abstract class DamageSpell extends Spell {
    public DamageSpell(String spellName, int power, String description, int manaCost) {
        super(spellName, power, description, manaCost);
    }

    protected void damage(Player player, Enemy enemy, Spell spell) {
        if (player.getPlayerCharacter().getMana() < spell.getManaCost()) {
            System.out.println("💤 " + MessageBundle.get("spells.damageSpell.lowMana",spell.getSpellName()));
            return;
        }
        if (!player.getPlayerType().equals(PlayerTypes.Mage)) {
            System.out.println("🚫 " + MessageBundle.get("spells.damageSpell.error.playerType",player.getPlayerCharacter()));
        }
        enemy.takeDamage(spell.takePower());
        player.loseMana(spell.getManaCost());
        System.out.println(MessageBundle.get("spells.damageSpell.castSpell",spell.getSymbol(),spell.getSpellName()));
        System.out.println("💥 " + spell.getDescription());

    }
}

package com.questoftherealm.spells;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.interfaces.Output;


public abstract class DamageSpell extends Spell {
    public DamageSpell(String spellName, int power, String description, int manaCost) {
        super(spellName, power, description, manaCost);
    }

    protected void damage(Player player, Enemy enemy, Spell spell, GameState state) {
        Output output = state.getGameServices().getOutput();
        if (player.getPlayerCharacter().getMana() < spell.getManaCost()) {
            output.println("💤 " + state.getMessages().getBundle().get("spells.damageSpell.lowMana", spell.getSpellName()));
            return;
        }
        if (!player.getPlayerType().equals(PlayerTypes.Mage)) {
            output.println("🚫 " + state.getMessages().getBundle().get("spells.damageSpell.error.playerType", player.getPlayerCharacter()));
        }
        enemy.takeDamage(spell.takePower(), state);
        player.loseMana(spell.getManaCost());
        output.println(state.getMessages().getBundle().get("spells.damageSpell.castSpell", spell.getSpellName(), spell.getSymbol()));
        output.println("💥 " + spell.getDescription());

    }
}

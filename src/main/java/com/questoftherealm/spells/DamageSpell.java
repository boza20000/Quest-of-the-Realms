package com.questoftherealm.spells;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.exceptions.NotEnoughManaException;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.server.ServerLogger;


public abstract class DamageSpell extends Spell {
    public DamageSpell(String spellName, int power, String description, int manaCost) {
        super(spellName, power, description, manaCost);
    }

    protected void damage(Player player, Enemy enemy, Spell spell, GameState state) {
        Output output = state.getGameServices().getOutput();
        if (!player.getPlayerType().equals(PlayerTypes.Mage)) {
            output.println("🚫 " + state.getMessages().getBundle().get("spells.damageSpell.error.playerType", player.getPlayerCharacter()));
            return;
        }
        try {
            player.loseMana(spell.getManaCost());
        } catch (NotEnoughManaException e) {
            ServerLogger.get().warn("Player " + player.getName() + " attempted spell " + spell.getSpellName() + " with insufficient mana", e);
            output.println("💤 " + state.getMessages().getBundle().get("spells.damageSpell.lowMana", spell.getSpellName()));
            return;
        }
        enemy.takeDamage(spell.takePower(), state);

        if (enemy.isDead()) {
            player.curTile(state).removeEnemy(enemy, state);
        }
        output.println(state.getMessages().getBundle().get("spells.damageSpell.castSpell", spell.getSpellName(), spell.getSymbol()));
        output.println("💥 " + spell.getDescription());

    }
}

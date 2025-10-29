package com.questoftherealm.spells;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.spells.interfaces.DamageSpell;

public class LightningBolt extends Spell implements DamageSpell {

    public LightningBolt() {
        super("Lightning Bolt", 30, "A crackling bolt of lightning strikes your enemy.", 7);
    }

    public void castLightning(Player player, Enemy enemy) {
        this.damage(player, enemy, this);
    }

    @Override
    public void cast(Player player, Enemy enemy) {
        castLightning(player, enemy);
    }

    @Override
    public String getSymbol() {
        return "⚡";
    }
}

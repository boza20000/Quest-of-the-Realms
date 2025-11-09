package com.questoftherealm.spells;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.localization.MessageBundle;

public class LightningBolt extends DamageSpell {

    public LightningBolt() {
        super(MessageBundle.get("spells.lightning.name"), 30, MessageBundle.get("spells.lightning.description"), 7);
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

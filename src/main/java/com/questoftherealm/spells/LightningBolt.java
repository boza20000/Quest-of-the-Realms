package com.questoftherealm.spells;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.game.GameState;
import com.questoftherealm.localization.MessageBundle;

public class LightningBolt extends DamageSpell {

    public LightningBolt() {
        super(MessageBundle.get("spells.lightning.name"), 30, MessageBundle.get("spells.lightning.description"), 7);
    }

    public void castLightning(Player player, Enemy enemy, GameState state) {
        this.damage(player, enemy, this, state);
    }

    @Override
    public void cast(Player player, Enemy enemy, GameState state) {
        castLightning(player, enemy, state);
    }

    @Override
    public String getSymbol() {
        return "⚡";
    }
}

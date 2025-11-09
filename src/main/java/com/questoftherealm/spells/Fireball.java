package com.questoftherealm.spells;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.localization.MessageBundle;

public class Fireball extends DamageSpell {

    public Fireball() {
        super(MessageBundle.get("spells.fireball.name"), 25, MessageBundle.get("spells.fireball.description"), 5);
    }

    public void castFireball(Player player, Enemy enemy) {
        this.damage(player, enemy, this);
    }

    @Override
    public void cast(Player player, Enemy enemy) {
        castFireball(player,enemy);
    }

    @Override
    public String getSymbol() {
        return "🔥";
    }

}

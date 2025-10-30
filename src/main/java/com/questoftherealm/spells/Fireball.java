package com.questoftherealm.spells;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.spells.interfaces.DamageSpell;

public class Fireball extends Spell implements DamageSpell {

    public Fireball() {
        super("Fireball", 25, "A blazing orb of fire that scorches your enemies.", 5);
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

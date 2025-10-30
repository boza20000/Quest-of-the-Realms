package com.questoftherealm.characters.characterInterfaces;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.spells.Spell;

public interface SpellCaster {
    void castSpell(Player player, Spell spell, Enemy target);
}

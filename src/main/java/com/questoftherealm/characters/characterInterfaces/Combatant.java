package com.questoftherealm.characters.characterInterfaces;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.game.GameState;

public interface Combatant {
    void attack(Enemy target, Player player, GameState state);
    void takeDamage(int damage,GameState state,Player player);
}
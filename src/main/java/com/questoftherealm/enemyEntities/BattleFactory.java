package com.questoftherealm.enemyEntities;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.game.Game;
import com.questoftherealm.game.GameState;

public class BattleFactory {

    public static Battle createBattle(Player player, Enemy enemy, GameState state) {
        return new Battle(player,enemy,state);
    }

}

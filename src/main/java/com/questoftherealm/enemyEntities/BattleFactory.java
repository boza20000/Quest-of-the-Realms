package com.questoftherealm.enemyEntities;

import com.questoftherealm.characters.player.Player;

public class BattleFactory {

    public static Battle createBattle(Player player, Enemy enemy) {
        return new Battle(player,enemy);
    }

//    public static Battle createBattle(Player player, List<Enemy> enemies) {
//        return new Battle(player, enemies);
//    }

}

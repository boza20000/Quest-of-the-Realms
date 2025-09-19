package com.questoftherealm.characters.EnemyEntities;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.maps.TileTypes;

import java.util.List;

public class BattleFactory {

    public static Battle createBattle(Player player, Enemy enemy) {
        return new Battle(player,enemy);
    }

//    public static Battle createBattle(Player player, List<Enemy> enemies) {
//        return new Battle(player, enemies);
//    }

}

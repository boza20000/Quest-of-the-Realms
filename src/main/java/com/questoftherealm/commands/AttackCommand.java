package com.questoftherealm.commands;

import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.game.Game;
import com.questoftherealm.maps.Tile;

public class AttackCommand extends Command {
    public AttackCommand() {
        super("attack");
    }

    @Override
    public void execute(String[] args) {
        String enemyName = args[1];
        Player player = Game.getPlayer();
        Tile curTile = Game.getGameMap().curZone(player.getX(), player.getY());
        try {
            Enemy choosenEnemy = curTile.getEnemy(enemyName);
            choosenEnemy.interact(player);
        }
        catch (Exception e){
            System.out.println("Battle not possible");
        }
    }
    @Override
    public String getDescription() {
        return "attack [enemy name]";
    }
}

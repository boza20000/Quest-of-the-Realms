package com.questoftherealm.commands;

import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.game.Game;
import com.questoftherealm.map.Tile;

public class AttackCommand extends Command {
    public AttackCommand() {
        super("attack");
    }

    @Override
    public void execute(String[] args) {
        Player player = Game.getPlayer();
        if(!makeSafe(args, player)){
            return;
        }
        String enemyName = args[1];
        Tile curTile = Game.getGameMap().curZone(player.getX(), player.getY());
        if (curTile == null) {
            System.out.println("You are in an undefined area.");
            return;
        }
        Enemy chosenEnemy = curTile.getEnemy(enemyName);
        if (chosenEnemy == null) {
            System.out.println("No enemy named '" + enemyName + "' here!");
            return;
        }
        boolean isKilled = false;
        try {
            isKilled = chosenEnemy.interact(player);
        } catch (Exception e) {
            System.out.println("Battle was unavailable");
        }
        if (isKilled) {
            int gold = 5;//improve
            int exp = 10;//improve
            curTile.removeEnemy(chosenEnemy);
            System.out.println("Successful battle! You receive " + gold + "Gold" + " and you receive " + exp + "XP.");
        }
    }

    @Override
    public String getDescription() {
        return "attack [enemy name] — engage an enemy in combat at your current location";
    }

    @Override
    public boolean makeSafe(String[] args, Player player) {
        if (args.length != 2) {
            System.out.println("Usage: " + getDescription());
            return false;
        }
        return playerBaseCheck(player);
    }

}

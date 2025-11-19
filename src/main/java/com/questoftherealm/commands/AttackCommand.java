package com.questoftherealm.commands;

import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.game.Game;
import com.questoftherealm.game.GameState;
import com.questoftherealm.map.Tile;

public class AttackCommand extends Command {
    public AttackCommand() {
        super("attack");
    }

    @Override
    public void execute(String[] args, Player player, GameState state) {
        if (!makeSafe(args, player,state)) {
            return;
        }
        String enemyName = args[1];
        Tile curTile = state.getMap().curZone(player.getX(), player.getY());
        if (curTile == null) {
            state.getGameServices().getOutput().println("You are in an undefined area.");
            return;
        }
        Enemy chosenEnemy = curTile.getEnemy(enemyName);
        if (chosenEnemy == null) {
            state.getGameServices().getOutput().println("No enemy named '" + enemyName + "' here!");
            return;
        }
        boolean isKilled = false;
        try {
            isKilled = chosenEnemy.interact(player,state);
        } catch (Exception e) {
            state.getGameServices().getOutput().println("Battle was unavailable");
        }
        if (isKilled) {
            int gold = 5;
            int exp = 10;
            player.addMoney(gold,state);
            player.addExp(exp);
            curTile.removeEnemy(chosenEnemy);
            state.getGameServices().getOutput().println("Successful battle! You receive " + gold + "Gold" + " and you receive " + exp + "XP.");
        }
       // state.getGameServices().getOutput().println(player.getName() + " attacked " + enemyName + " at " + player.getPosition());
    }

    @Override
    public String getDescription() {
        return "attack [enemy name] — engage an enemy in combat at your current location";
    }

    @Override
    public boolean makeSafe(String[] args, Player player,GameState state) {
        if (args.length != 2) {
            state.getGameServices().getOutput().println("Usage: " + getDescription());
            return false;
        }
        return playerBaseCheck(player,state);
    }

}

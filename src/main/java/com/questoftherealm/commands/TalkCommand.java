package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.enemyEntities.entities.TraderNPC;
import com.questoftherealm.friendlyEntities.Entities.Elder;
import com.questoftherealm.friendlyEntities.Entities.Villager;
import com.questoftherealm.game.Game;
import com.questoftherealm.game.GameConstants;
import com.questoftherealm.game.Position;
import com.questoftherealm.map.Tile;

public class TalkCommand extends Command {
    public TalkCommand() {
        super("talk");
    }

    @Override
    public boolean makeSafe(String[] args, Player player) {
        if (args.length != 2) {
            System.out.println("Usage: " + getDescription());
            return false;
        }
        return playerBaseCheck(player);
    }

    @Override
    public void execute(String[] args) {
        Player player = Game.getPlayer();
        if (!makeSafe(args, player)) {
            return;
        }

        String target = args[1].toLowerCase();
        Position curPos = new Position(player.getX(), player.getY());
        switch (target) {
            case "elder" -> {
                if (!Elder.isHasTalked() && curPos.equals(GameConstants.Castle)) {
                    Elder elder = new Elder();
                    elder.talk(player);
                } else if (!Elder.isHasTalked()) {
                    System.out.println("In order to talk to the Elder go to the castle.");
                } else {
                    System.out.println("You have already done that.");
                }
            }
            case "trader" -> {
                TraderNPC trader = new TraderNPC();
                Tile curTile = Game.getGameMap().curZone(player.getX(), player.getY());
                if (curTile.getEnemies().contains(trader)) {
                    trader.talk(player);
                } else {
                    System.out.println("No trader spotted in this zone");
                }

            }
            case "villager" -> {
                Villager villager = new Villager();
                villager.talk(player);
            }
            default -> throw new IllegalArgumentException("No such Target to talk to");
        }
    }

    @Override
    public String getDescription() {
        return "you talk with NPC in order to trade or exchange information";
    }
}

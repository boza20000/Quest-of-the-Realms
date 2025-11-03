package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.friendlyEntities.Entities.Elder;
import com.questoftherealm.friendlyEntities.Entities.King;
import com.questoftherealm.friendlyEntities.Entities.Villager;
import com.questoftherealm.friendlyEntities.NpcType;
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
        Tile curTile = Game.getGameMap().curZone(player.getX(), player.getY());

        switch (target) {
            case "elder" -> {
                Elder elder = (Elder) curTile.getNpcByType(NpcType.Elder);
                if (elder == null) {
                    System.out.println("The Elder is not present.");
                    break;
                }

                if (!elder.isHasTalked() && curPos.equals(GameConstants.Castle)) {
                    elder.talk(player, false);
                } else if (!elder.isHasTalked()) {
                    System.out.println("In order to talk to the Elder, go to the castle.");
                } else {
                    System.out.println("The Elder has nothing else to say.");
                }
                break;
            }

            case "trader" -> {
                System.out.println("No trader spotted in this zone.");
                break;
            }

            case "king" -> {
                King king = (King) curTile.getNpcByType(NpcType.King);
                if (king == null) {
                    System.out.println("No King nearby.");
                    break;
                }
                king.talk(player, false);
                break;
            }

            case "villager" -> {
                Villager villager = (Villager) curTile.getNpcByType(NpcType.Villager);
                if (villager == null) {
                    System.out.println("No Villager nearby.");
                    break;
                }
                villager.talk(player, false);
                break;
            }

            default -> throw new IllegalArgumentException("No such target to talk to: " + target);
        }
    }

    @Override
    public String getDescription() {
        return "you talk with NPCs in order to trade or exchange information";
    }
}

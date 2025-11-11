package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.friendlyEntities.Npc;
import com.questoftherealm.friendlyEntities.NpcType;
import com.questoftherealm.game.Game;
import com.questoftherealm.game.Position;
import com.questoftherealm.map.Tile;

import java.util.Locale;

public class TalkCommand extends Command {

    public TalkCommand() {
        super("talk");
    }

    @Override
    public boolean makeSafe(String[] args, Player player) {
        if (args.length != 2) {
            System.out.println("Usage: talk <npc>");
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
        String target = args[1].toLowerCase(Locale.ROOT);
        Tile curTile = Game.getGameMap().curZone(player.getX(), player.getY());
        NpcType npcType = parseNpcType(target);

        if (npcType == null) {
            System.out.println("Unknown NPC: " + target);
            return;
        }
        Npc npc = curTile.getNpcByType(npcType);

        if (npc == null) {
            System.out.println("No " + npcType.name().toLowerCase(Locale.ROOT) + " nearby.");
            return;
        }
        npc.talk(player, false);
    }

    private NpcType parseNpcType(String name) {
        try {
            return NpcType.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public String getDescription() {
        return "Talk to nearby NPCs to trade or exchange information.";
    }
}

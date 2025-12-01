package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.friendlyEntities.Npc;
import com.questoftherealm.friendlyEntities.NpcType;
import com.questoftherealm.game.GameState;
import com.questoftherealm.map.Tile;

import java.util.Locale;

public class TalkCommand extends Command {

    public TalkCommand() {
        super("talk");
    }

    @Override
    public boolean makeSafe(String[] args, Player player, GameState state) {
        if (args.length != 2) {
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("talk.usage", getDescription(state)));
            return false;
        }
        return playerBaseCheck(player,state);
    }

    @Override
    public void execute(String[] args, Player player, GameState state) {
        if (!makeSafe(args, player, state)) {
            return;
        }
        String target = args[1].toLowerCase(Locale.ROOT);
        Tile curTile = state.getMap().curZone(player.getX(), player.getY());
        NpcType npcType = parseNpcType(target);

        if (npcType == null) {
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("talk.error.unknownNpc", target));
            return;
        }
        Npc npc = curTile.getNpcByType(npcType);

        if (npc == null) {
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("talk.error.noNpcNearby", npcType.name().toLowerCase(Locale.ROOT)));
            return;
        }
        npc.talk(state, player, false);
    }

    private NpcType parseNpcType(String name) {
        try {
            return NpcType.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public String getDescription(GameState state) {
        return state.getMessages().getBundle().get("talk.description");
    }
}
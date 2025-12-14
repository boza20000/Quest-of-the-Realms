package com.questoftherealm.friendlyEntities.Entities;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.friendlyEntities.Npc;
import com.questoftherealm.friendlyEntities.NpcType;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.interaction.MissionInteractions;
import com.questoftherealm.items.Item;

import java.util.HashMap;

public class Trader extends Npc {
    private Output output;
    private HashMap<Item,Integer> itemsForSale;

    public Trader(String id, GameState state, MissionInteractions missionInteractions) {
        super(NpcType.TRADER, id, state, missionInteractions);
        this.output = state.getGameServices().getOutput();
    }

    @Override
    public void talk(GameState state, Player player, boolean isSimulation) {

    }
}

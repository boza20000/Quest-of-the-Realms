package com.questoftherealm.game;

import com.questoftherealm.exceptions.NpcInitializationFailed;
import com.questoftherealm.friendlyEntities.Entities.Elder;
import com.questoftherealm.friendlyEntities.Entities.King;
import com.questoftherealm.friendlyEntities.Entities.Trader;
import com.questoftherealm.friendlyEntities.Entities.Villager;
import com.questoftherealm.interaction.MissionInteractions;
import com.questoftherealm.localization.MessageBundle;
import com.questoftherealm.map.Tile;

public class NpcInitializer {
    public void registerAll(GameState state) {
        MissionInteractions interactions = new MissionInteractions(state);
        MessageBundle bundle = state.getMessages().getBundle();
        try {
            //castle
            Tile castleTile = state.getMap().curZone(GameConstants.Castle.x(), GameConstants.Castle.y());
            registerElder(state, interactions, castleTile, bundle.get("npc.elder"));
            registerKing(state, interactions, castleTile, bundle.get("npc.king"));

            //north village 1
            Tile villagerTileNorth1 = state.getMap().curZone(GameConstants.NorthVillage_1.x(), GameConstants.NorthVillage_1.y());
            registerVillager(state, interactions, villagerTileNorth1, bundle.get("npc.northVillager1"));

            //north village 2
            Tile villagerTileNorth2 = state.getMap().curZone(GameConstants.NorthVillage_2.x(), GameConstants.NorthVillage_2.y());
            registerVillager(state, interactions, villagerTileNorth2, bundle.get("npc.northVillager2"));

            //random wandering trader
            RandomService randomService = state.getGameServices().getRandom();
            Tile randomTile = state.getMap().curZone(randomService.randomInt(GameConstants.MAP_END), randomService.randomInt(GameConstants.MAP_END));
            registerTrader(state, interactions, randomTile, bundle.get("npc.trader"));
        }
        catch (Exception e){
            throw new NpcInitializationFailed(bundle.get("game.npc.init.error"));
        }
    }

    private void registerVillager(GameState state, MissionInteractions m, Tile tile, String id) {
        Villager villager = new Villager(state.getMessages().getBundle().get(id), state, m);
        tile.registerNpc(villager);

    }

    private void registerKing(GameState state, MissionInteractions m, Tile tile, String id) {
        King king = new King(id, state, m);
        tile.registerNpc(king);
    }

    private void registerElder(GameState state, MissionInteractions m, Tile tile, String id) {
        Elder elder = new Elder(id, state, m);
        tile.registerNpc(elder);
    }

    private void registerTrader(GameState state, MissionInteractions m, Tile tile, String id) {
        Trader trader = new Trader(id, state, m);
        tile.registerNpc(trader);
    }


}

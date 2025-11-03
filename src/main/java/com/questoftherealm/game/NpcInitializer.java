package com.questoftherealm.game;

import com.questoftherealm.friendlyEntities.Entities.Elder;
import com.questoftherealm.friendlyEntities.Entities.King;
import com.questoftherealm.friendlyEntities.Entities.Villager;
import com.questoftherealm.map.Tile;
import com.questoftherealm.map.Map;

public class NpcInitializer {

    public static void registerAll(Map gameMap) {
        //castle
        Tile castleTile = gameMap.curZone(GameConstants.Castle.x(),GameConstants.Castle.y());
        castleTile.addNpc(new Elder());
        castleTile.addNpc(new King());
        //north village 1
        Tile villagerTileNorth1 = gameMap.curZone(GameConstants.NorthVillage_1.x(), GameConstants.NorthVillage_1.y());
        villagerTileNorth1.addNpc(new Villager());
        //north village 2
        Tile villagerTileNorth2 = gameMap.curZone(GameConstants.NorthVillage_2.x(), GameConstants.NorthVillage_2.y());
        villagerTileNorth2.addNpc(new Villager());

    }


}

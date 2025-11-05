package com.questoftherealm.game;

import com.questoftherealm.friendlyEntities.Entities.Elder;
import com.questoftherealm.friendlyEntities.Entities.King;
import com.questoftherealm.friendlyEntities.Entities.Villager;
import com.questoftherealm.friendlyEntities.Npc;
import com.questoftherealm.map.Tile;
import com.questoftherealm.map.Map;

public class NpcInitializer {

    public static void registerAll(Map gameMap) {
        Npc villager1 = new Villager("North_Villager_1");
        Villager villager2 = new Villager("North_Villager_2");
        King king = new King("King_MainHall");
        Elder elder = new Elder("Elder_Castle");

        //castle
        Tile castleTile = gameMap.curZone(GameConstants.Castle.x(),GameConstants.Castle.y());
        castleTile.registerNpc(elder);
        castleTile.registerNpc(king);

        //north village 1
        Tile villagerTileNorth1 = gameMap.curZone(GameConstants.NorthVillage_1.x(), GameConstants.NorthVillage_1.y());
        villagerTileNorth1.registerNpc(villager1);

        //north village 2
        Tile villagerTileNorth2 = gameMap.curZone(GameConstants.NorthVillage_2.x(), GameConstants.NorthVillage_2.y());
        villagerTileNorth2.registerNpc(villager2);

    }


}

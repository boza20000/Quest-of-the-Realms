package com.questoftherealm.expeditions.missions;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.game.Game;
import com.questoftherealm.game.GameConstants;
import com.questoftherealm.game.Position;

public class Explore_Nearby_Forests extends Mission {

    public Explore_Nearby_Forests() {
        super("Find the creatures", "Explore the forest near the village and search for potential camp");
    }

    @Override
    public boolean checkCompletion() {
        Player player = Game.getPlayer();
        Position pos = new Position(player.getX(),player.getY());
        if(pos.equals(GameConstants.Goblin_Camp)){
            complete();
            return true;
        }
        return false;
    }
}

package com.questoftherealm.expeditions.missions;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.game.Game;
import com.questoftherealm.game.GameConstants;
import com.questoftherealm.game.Position;

public class Infiltrate_the_Camp extends Mission {
    public Infiltrate_the_Camp() {
        super("Infiltrate the Camp", "Sneak into a goblin camp to learn their plans.");
    }

    @Override
    public boolean checkCompletion() {
        Player player = Game.getPlayer();
        Position pos = new Position(player.getX(),player.getY());
        if(pos.equals(GameConstants.Goblin_Camp) && Explore_Nearby_Forests.campFound){
            complete();
            return true;
        }
        return false;
    }
}

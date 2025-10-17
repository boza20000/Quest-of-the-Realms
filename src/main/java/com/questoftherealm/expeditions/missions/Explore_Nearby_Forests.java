package com.questoftherealm.expeditions.missions;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.expeditions.quests.GoblinAmbush;
import com.questoftherealm.game.Game;
import com.questoftherealm.game.GameConstants;
import com.questoftherealm.game.Position;

public class Explore_Nearby_Forests extends Mission {

    public static boolean campFound = false;

    public Explore_Nearby_Forests() {
        super("Find the creatures", "Explore the forest near the village and search for potential camp");
    }

    @Override
    public boolean checkCompletion() {
        if(isCompleted())return true;
        Player player = Game.getPlayer();
        Position pos = new Position(player.getX(),player.getY());
        if(Game.getPlayer().getCurQuest() instanceof GoblinAmbush && pos.equals(GameConstants.Goblin_Camp)){
            complete();
            return true;
        }
        return false;
    }
}

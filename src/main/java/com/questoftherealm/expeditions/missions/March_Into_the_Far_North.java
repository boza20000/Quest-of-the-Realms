package com.questoftherealm.expeditions.missions;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.expeditions.quests.FinalBattle;
import com.questoftherealm.game.Game;
import com.questoftherealm.game.GameConstants;

public class March_Into_the_Far_North extends Mission {
    public March_Into_the_Far_North(Player player) {
        super("March Into the Far North Mountains", "Lead your army into the frozen lands of the secret Fifth Kingdom.",player);
    }

    @Override
    public boolean checkCompletion() {
        if(isCompleted())return true;
        if (player.getCurQuest() instanceof FinalBattle && player.getPosition().equals(GameConstants.FarNorthMountain)) {
            complete();
            return true;
        }
        return false;
    }
}

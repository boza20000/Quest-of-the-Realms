package com.questoftherealm.expeditions.missions;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.expeditions.quests.RiseOfTheGoblinThreat;
import com.questoftherealm.game.Game;
import com.questoftherealm.game.GameConstants;

public class Warn_the_Castle extends Mission {
    public Warn_the_Castle(Player player) {
        super("Warn the Castle", "Return to the capital and inform the king of the looming goblin threat.",player);
    }

    @Override
    public boolean checkCompletion() {
        if(isCompleted())return true;
        if(player.getCurQuest() instanceof RiseOfTheGoblinThreat  && player.getPosition().equals(GameConstants.Castle)){
            complete();
            return true;
        }
        return false;
    }
}

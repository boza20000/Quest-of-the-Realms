package com.questoftherealm.expeditions.missions;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.expeditions.Quest;
import com.questoftherealm.expeditions.QuestFactory;
import com.questoftherealm.expeditions.quests.NorthExploration;
import com.questoftherealm.expeditions.quests.StartQuest;
import com.questoftherealm.game.Game;
import com.questoftherealm.game.GameConstants;

public class Travel_North extends Mission {
    public Travel_North() {
        super("Travel North", "Journey through forests and mountains to reach the northern region.");
    }

    @Override
    public boolean checkCompletion() {
        if(isCompleted())return true;
        Player player = Game.getPlayer();
        int y = player.getY();
        if ((Game.getPlayer().getCurQuest() instanceof NorthExploration) && y <= GameConstants.North_Y) {
            complete();
            return true;
        }
        return false;
    }
}

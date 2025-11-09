package com.questoftherealm.expeditions.missions;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.expeditions.quests.RiseOfTheGoblinThreat;
import com.questoftherealm.game.Game;
import com.questoftherealm.game.GameConstants;

public class Warn_the_Castle extends Mission {
    public Warn_the_Castle(Player player) {
        super("Warn the Castle",
                "Return to the capital and inform the king of the looming goblin threat.",
                player,
                (p,m)->p.getCurQuest() instanceof RiseOfTheGoblinThreat  && p.getPosition().equals(GameConstants.Castle)
                );
    }
}

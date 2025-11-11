package com.questoftherealm.expeditions.missions;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.expeditions.quests.GoblinAmbush;
import com.questoftherealm.expeditions.quests.NorthExploration;
import com.questoftherealm.game.Game;
import com.questoftherealm.game.GameConstants;
import com.questoftherealm.game.Position;

public class Infiltrate_the_Camp extends Mission {
    public Infiltrate_the_Camp(Player player) {
        super("Infiltrate the Camp",
                "Sneak into a goblin camp to learn their plans.",
                player,
                (p,m)->p.getCurQuest() instanceof GoblinAmbush q && p.getPosition().equals(GameConstants.Goblin_Camp) && q.isCampFound()
        );
    }
    public Infiltrate_the_Camp(){}
}

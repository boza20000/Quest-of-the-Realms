package com.questoftherealm.expeditions.missions;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.expeditions.quests.GoblinAmbush;
import com.questoftherealm.game.Game;
import com.questoftherealm.game.GameConstants;
import com.questoftherealm.game.Position;

public class Explore_Nearby_Forests extends Mission {
    public Explore_Nearby_Forests(Player player) {
        super("Find the creatures",
                "Explore the forest near the village and search for potential camp",
                player,
                (p,m)->p.getCurQuest() instanceof GoblinAmbush && p.getPosition().equals(GameConstants.Goblin_Camp)
                );
    }
    public Explore_Nearby_Forests(){}
}

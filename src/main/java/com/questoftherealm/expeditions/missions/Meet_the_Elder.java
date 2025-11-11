package com.questoftherealm.expeditions.missions;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.quests.StartQuest;
import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.friendlyEntities.NpcType;
import com.questoftherealm.game.Game;

public class Meet_the_Elder extends Mission {
    public Meet_the_Elder(Player player) {
        super("Meet the Elder",
                "Speak with the castle elder about strange rumors from the north.",
                player,
                (p, m) -> p.getCurQuest() instanceof StartQuest q
                        && q.isElderHasTalked()
        );
    }
    public Meet_the_Elder(){}
}

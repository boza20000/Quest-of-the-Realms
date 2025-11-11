package com.questoftherealm.expeditions.quests;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.Quest;
import com.questoftherealm.expeditions.missions.Gather_Supplies;
import com.questoftherealm.expeditions.missions.Meet_the_Elder;

import java.util.List;

public class StartQuest extends Quest {
    private boolean elderHasTalked = false;

    public StartQuest(Player player) {
        super("Start Journey",
                List.of(
                        new Meet_the_Elder(player),
                        new Gather_Supplies(player)
                ),
                "Your journey begins. The villagers whisper of dangers lurking in the north.",
                player
        );
    }

    public StartQuest() {
        super("Start Journey",
                List.of(
                        new Meet_the_Elder(null),
                        new Gather_Supplies(null)
                ),
                "Your journey begins. The villagers whisper of dangers lurking in the north.",
                null
        );
    }

    public boolean isElderHasTalked() {
        return elderHasTalked;
    }

    public void setElderHasTalked(boolean elderHasTalked) {
        this.elderHasTalked = elderHasTalked;
    }
}

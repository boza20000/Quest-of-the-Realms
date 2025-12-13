package com.questoftherealm.expeditions.quest.quests;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.quest.Quest;
import com.questoftherealm.expeditions.quest.QuestTypes;
import com.questoftherealm.game.GameState;

public class StartQuest extends Quest {
    private boolean elderHasTalked = false;

    public StartQuest(Player player, GameState state) {
        super(QuestTypes.START_QUEST,player,state);
    }
    public StartQuest() {
        super();
    }

    public boolean isElderHasTalked() {
        return elderHasTalked;
    }

    public void setElderHasTalked(boolean elderHasTalked) {
        this.elderHasTalked = elderHasTalked;
    }
}

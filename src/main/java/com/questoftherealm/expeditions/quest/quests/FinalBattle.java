package com.questoftherealm.expeditions.quest.quests;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.quest.Quest;
import com.questoftherealm.expeditions.quest.QuestTypes;
import com.questoftherealm.game.GameState;

public class FinalBattle extends Quest {
    private boolean isBreached = false;
    private boolean isDefeated = false;

    public FinalBattle(Player player, GameState state) {
        super(QuestTypes.FINAL_BATTLE,player,state);
    }
    protected FinalBattle() {
        super();
    }

    public boolean isDefeated() {
        return isDefeated;
    }

    public void setDefeated(boolean defeated) {
        isDefeated = defeated;
    }

    public boolean isBreached() {
        return isBreached;
    }

    public void setBreached(boolean breached) {
        isBreached = breached;
    }
}

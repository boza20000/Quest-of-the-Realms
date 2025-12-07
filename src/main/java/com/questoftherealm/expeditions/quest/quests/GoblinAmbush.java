package com.questoftherealm.expeditions.quest.quests;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.quest.Quest;
import com.questoftherealm.expeditions.quest.QuestTypes;
import com.questoftherealm.game.GameState;

public class GoblinAmbush extends Quest {
    private boolean playerAmbushed = false;
    private boolean campFound = false;
    private boolean playerEscapedAmbush = false;

    public GoblinAmbush(Player player, GameState state) {
        super(QuestTypes.GOBLIN_AMBUSH,player,state);
    }
    protected GoblinAmbush() {
        super();
    }


    public boolean isPlayerAmbushed() {
        return playerAmbushed;
    }

    public void setPlayerAmbushed(boolean ambushed) {
        playerAmbushed = ambushed;
    }

    public boolean isCampFound() {
        return campFound;
    }

    public void setCampFound(boolean b) {
        campFound = b;
    }

    public boolean isPlayerEscapedAmbush() {
        return playerEscapedAmbush;
    }

    public void setPlayerEscapedAmbush(boolean playerEscapedAmbush) {
        this.playerEscapedAmbush = playerEscapedAmbush;
    }
}

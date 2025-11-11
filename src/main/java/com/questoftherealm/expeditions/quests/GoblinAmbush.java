package com.questoftherealm.expeditions.quests;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.Quest;
import com.questoftherealm.expeditions.missions.*;

import java.util.List;

public class GoblinAmbush extends Quest {
    private boolean playerAmbushed = false;
    private boolean campFound = false;
    private boolean playerEscapedAmbush = false;

    public GoblinAmbush(Player player) {
        super(
                "Goblin Ambush",
                List.of(
                        new Explore_Nearby_Forests(player),
                        new Infiltrate_the_Camp(player),
                        new Ambushed(player),
                        new Escape_to_Safety(player)
                ),
                "You stumble upon a goblin war camp, but your presence does not go unnoticed...",
                player
        );
    }

    public GoblinAmbush() {
        super(
                "Goblin Ambush",
                List.of(
                        new Explore_Nearby_Forests(null),
                        new Infiltrate_the_Camp(null),
                        new Ambushed(null),
                        new Escape_to_Safety(null)
                ),
                "You stumble upon a goblin war camp, but your presence does not go unnoticed...",
                null
        );
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

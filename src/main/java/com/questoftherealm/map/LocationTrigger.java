package com.questoftherealm.map;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.game.Position;

public class LocationTrigger{
    private final Position position;
    private final TriggerAction action;

    public LocationTrigger(Position position, TriggerAction action) {
        this.position = position;
        this.action = action;
    }

    public boolean isAtPosition(Position p) {
        return this.position.equals(p);
    }

    public void trigger(Player player){
        action.execute(player);
    }
}


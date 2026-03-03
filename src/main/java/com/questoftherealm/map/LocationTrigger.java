package com.questoftherealm.map;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.game.Position;
import com.questoftherealm.map.interfaces.TriggerAction;

public class LocationTrigger{
    private final Position position;
    private final TriggerAction action;
    private volatile boolean isExecuted;

    public LocationTrigger(Position position, TriggerAction action) {
        this.position = position;
        this.action = action;
        this.isExecuted = false;
    }

    public boolean isAtPosition(Position p) {
        return this.position.equals(p);
    }

    public synchronized void trigger(Player player){
        if(action.execute(player)){
            setExecuted(true);
        }
    }

    public boolean isExecuted() {
        return isExecuted;
    }

    public void setExecuted(boolean executed) {
        isExecuted = executed;
    }

}


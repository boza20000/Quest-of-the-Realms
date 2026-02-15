package com.questoftherealm.game;

import com.questoftherealm.game.interfaces.Clock;

public class ServerClock implements Clock {
    @Override
    public long now() {
        return System.currentTimeMillis();
    }
}

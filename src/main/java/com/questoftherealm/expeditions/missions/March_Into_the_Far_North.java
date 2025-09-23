package com.questoftherealm.expeditions.missions;

import com.questoftherealm.expeditions.Mission;

public class March_Into_the_Far_North extends Mission {
    public March_Into_the_Far_North() {
        super("March Into the Far North", "Lead your army into the frozen lands of the secret Fifth Kingdom.");
    }
    @Override
    public boolean checkCompletion() {
        return false;
    }
}

package com.questoftherealm.expeditions.missions;

import com.questoftherealm.expeditions.Mission;

public class Talk_To_Survivors extends Mission {

    public Talk_To_Survivors(){
        super("Survivors","Talk to the survivors from the north village.");
    }


    @Override
    public boolean checkCompletion() {
        return false;
    }
}

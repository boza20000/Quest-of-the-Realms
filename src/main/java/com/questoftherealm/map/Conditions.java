package com.questoftherealm.map;

import com.questoftherealm.expeditions.missions.Missions;
import com.questoftherealm.expeditions.quest.Quest;
import com.questoftherealm.map.interfaces.TriggerCondition;

public class Conditions {

    public static TriggerCondition of(Class<? extends Quest> q,
                                    Missions m) {
        return player ->
                q.isInstance(player.getCurQuest()) &&
                        m.equals(player.getCurMission().getMissionType());
    }
}

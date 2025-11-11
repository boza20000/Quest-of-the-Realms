package com.questoftherealm.expeditions;

import com.questoftherealm.expeditions.interfaces.MissionCondition;
import com.questoftherealm.expeditions.missions.*;

public class MissionConditionFactory {

    public static MissionCondition getCondition(MissionConditionType type) {
        if (type == null || type == MissionConditionType.NONE || type == MissionConditionType.CUSTOM_CHECK_COMPLETION) {
            return null;
        }
        return type.getCondition();
    }

    public static MissionConditionType getTypeForMission(Mission mission) {
        if (mission instanceof Meet_the_Elder) return MissionConditionType.MEET_ELDER;
        if (mission instanceof Gather_Supplies) return MissionConditionType.GATHER_SUPPLIES;
        if (mission instanceof Travel_North) return MissionConditionType.TRAVEL_NORTH;
        if (mission instanceof Infiltrate_the_Camp) return MissionConditionType.INFILTRATE_CAMP;
        if (mission instanceof Ambushed) return MissionConditionType.AMBUSHED;
        if (mission instanceof Escape_to_Safety) return MissionConditionType.ESCAPE_TO_SAFETY;
        if (mission instanceof Explore_Nearby_Forests) return MissionConditionType.EXPLORE_FORESTS;
        if (mission instanceof Warn_the_Castle) return MissionConditionType.WARN_CASTLE;
        if (mission instanceof Defeat_the_Goblin_General) return MissionConditionType.DEFEAT_GENERAL;
        if (mission instanceof March_Into_the_Far_North) return MissionConditionType.MARCH_FAR_NORTH;
        if (mission instanceof Breach_the_Stronghold) return MissionConditionType.BREACH_STRONGHOLD;
        if (mission instanceof Defeat_the_Goblin_King) return MissionConditionType.DEFEAT_KING;
        if (mission instanceof Investigate_Northern_Villages || mission instanceof Assemble_an_Army) {
            return MissionConditionType.CUSTOM_CHECK_COMPLETION;
        }
        return MissionConditionType.NONE;
    }

    public static MissionConditionType getType(MissionCondition condition) {
        return null;
    }
}
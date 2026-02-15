package com.questoftherealm.map;

import com.questoftherealm.expeditions.missions.*;
import com.questoftherealm.expeditions.quest.quests.FinalBattle;
import com.questoftherealm.expeditions.quest.quests.GoblinAmbush;
import com.questoftherealm.expeditions.quest.quests.NorthExploration;
import com.questoftherealm.expeditions.quest.quests.RiseOfTheGoblinThreat;
import com.questoftherealm.map.interfaces.TriggerCondition;

public class TriggerConditions {
    public static final TriggerCondition NORTH_VILLAGE = Conditions.of(NorthExploration.class,Missions.INVESTIGATE_VILLAGES);
    public static final TriggerCondition GOBLIN_CAMP_SEARCH = Conditions.of(GoblinAmbush.class, Missions.EXPLORE_FORESTS);
    public static final TriggerCondition GOBLIN_CAMP_INFILTRATION = Conditions.of(GoblinAmbush.class, Missions.AMBUSHED);
    public static final TriggerCondition GOBLIN_CAMP_ESCAPE = Conditions.of(GoblinAmbush.class, Missions.ESCAPE_TO_SAFETY);
    public static final TriggerCondition  ARMY_ASSEMBLING_KNIGHTS = Conditions.of(RiseOfTheGoblinThreat.class, Missions.ASSEMBLE_ARMY);
    public static final TriggerCondition  ARMY_ASSEMBLING_ARCHERS = Conditions.of(RiseOfTheGoblinThreat.class, Missions.ASSEMBLE_ARMY);
    public static final TriggerCondition  ARMY_ASSEMBLING_MAGES = Conditions.of(RiseOfTheGoblinThreat.class, Missions.ASSEMBLE_ARMY);
    public static final TriggerCondition ARMY_FIGHT = Conditions.of(RiseOfTheGoblinThreat.class, Missions.DEFEAT_GENERAL);
    public static final TriggerCondition GOBLIN_CAVE = Conditions.of(FinalBattle.class, Missions.BREACH_STRONGHOLD);
}

package com.questoftherealm.expeditions.quest;

import com.questoftherealm.expeditions.missions.Missions;
import com.questoftherealm.game.GameState;

import java.util.List;

public enum QuestTypes {
    START_QUEST(
            "quest.start.name",
            "quest.start.description",
            List.of(Missions.MEET_ELDER, Missions.GATHER_SUPPLIES)
    ),

    NORTH_EXPLORATION(
            "quest.north_exploration.name",
            "quest.north_exploration.description",
            List.of(Missions.TRAVEL_NORTH, Missions.INVESTIGATE_VILLAGES)
    ),

    GOBLIN_AMBUSH(
            "quest.goblin_ambush.name",
            "quest.goblin_ambush.description",
            List.of(Missions.INFILTRATE_CAMP, Missions.AMBUSHED, Missions.ESCAPE_TO_SAFETY, Missions.EXPLORE_FORESTS)
    ),

    RISE_OF_THE_GOBLIN_THREAT(
            "quest.rise_of_goblin_threat.name",
            "quest.rise_of_goblin_threat.description",
            List.of(Missions.WARN_CASTLE, Missions.ASSEMBLE_ARMY, Missions.DEFEAT_GENERAL)
    ),

    FINAL_BATTLE(
            "quest.final_battle.name",
            "quest.final_battle.description",
            List.of(Missions.MARCH_FAR_NORTH, Missions.BREACH_STRONGHOLD, Missions.DEFEAT_KING)
    );
    private final String name;
    private final String description;
    private final List<Missions> missions;

    QuestTypes(String name, String description, List<Missions> missions) {
        this.missions = missions;
        this.description = description;
        this.name = name;
    }

    public List<Missions> getMissions() {
        return missions;
    }

    public String getName(GameState state) {
        return state.getMessages().getBundle().get(name);
    }

    public String getDescription(GameState state) {
        return state.getMessages().getBundle().get(description);
    }
}

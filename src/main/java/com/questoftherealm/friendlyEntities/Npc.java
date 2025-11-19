package com.questoftherealm.friendlyEntities;

import com.questoftherealm.friendlyEntities.FriendInterfaces.Friendly;
import com.questoftherealm.game.GameState;
import com.questoftherealm.interaction.MissionInteractions;

import java.util.Objects;

public abstract class Npc implements Friendly {
    private final NpcType type;
    private String id;
    private GameState state;
    private MissionInteractions missionInteractions;

    public Npc(NpcType type, String id, GameState state, MissionInteractions missionInteractions) {
        this.type = type;
        this.id = id;
        this.missionInteractions = missionInteractions;
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public NpcType getType() {
        return type;
    }

    public GameState getState() {
        return state;
    }

    public MissionInteractions getMissionInteractions() {
        return missionInteractions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Npc npc)) return false;
        return Objects.equals(id, npc.id) && Objects.equals(type, npc.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, id);
    }

}

package com.questoftherealm.friendlyEntities;

import com.questoftherealm.friendlyEntities.FriendInterfaces.Friendly;
import java.util.Objects;

public abstract class Npc implements Friendly {
   private final NpcType type;
   private String id ;

    public Npc(NpcType type, String id) {
        this.type = type;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public NpcType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Npc npc)) return false;
        return Objects.equals(id, npc.id) && Objects.equals(type,npc.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, id);
    }

    public abstract boolean isHasTalked();
}

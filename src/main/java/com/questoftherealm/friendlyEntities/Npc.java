package com.questoftherealm.friendlyEntities;

import com.questoftherealm.friendlyEntities.FriendInterfaces.Friendly;

import java.util.Objects;

public abstract class Npc implements Friendly {
   private final NpcType type;
   private int id ;
   private static int idGenerator = 1;

    public Npc(NpcType type) {
        this.type = type;
        this.id = idGenerator++;
    }

    public int getId() {
        return id;
    }

    public NpcType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Npc npc = (Npc) o;
        return id == npc.id && type == npc.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, id);
    }

    public abstract boolean isHasTalked();
}

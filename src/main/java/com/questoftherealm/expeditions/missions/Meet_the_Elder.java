package com.questoftherealm.expeditions.missions;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.quests.StartQuest;
import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.friendlyEntities.Npc;
import com.questoftherealm.friendlyEntities.NpcType;
import com.questoftherealm.game.Game;
import com.questoftherealm.map.Tile;

public final class Meet_the_Elder extends Mission {
    public Meet_the_Elder(Player player) {
        super("Meet the Elder", "Speak with the castle elder about strange rumors from the north.",player);
    }

    @Override
    public boolean checkCompletion() {
        if (isCompleted()) return true;
        if (player.getCurQuest() instanceof StartQuest && !isCompleted()) {
            Tile curTile = Game.getGameMap().curZone(player.getX(), player.getY());
            Npc elder = curTile.getNpcByType(NpcType.Elder);
            if (elder != null && elder.isHasTalked()) {
                complete();
                return true;
            }
        }
        return false;
    }

}

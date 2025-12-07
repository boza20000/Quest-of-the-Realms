package com.questoftherealm.friendlyEntities.Entities;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.quest.quests.RiseOfTheGoblinThreat;
import com.questoftherealm.friendlyEntities.NpcType;
import com.questoftherealm.friendlyEntities.Npc;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.interaction.MissionInteractions;
import com.questoftherealm.interaction.SlowPrinter;

public class King extends Npc {
    private Output output;

    public King(String id, GameState state, MissionInteractions missionInteractions) {
        super(NpcType.KING, id, state, missionInteractions);
        this.output = state.getGameServices().getOutput();
    }

    @Override
    public void talk(GameState state, Player player, boolean isSimulation) {
        if (player.getCurQuest() instanceof RiseOfTheGoblinThreat q && !q.isReportedToKing()) {
            if (isSimulation) {
                q.setReportedToKing(true);
                return;
            }
            kingDialog(state);
            q.setReportedToKing(true);
        } else {
            output.println(state.getMessages().getBundle().get("king.talked"));
        }
    }

    private void kingDialog(GameState state) {
        SlowPrinter slowPrinter = new SlowPrinter(state);
        slowPrinter.slowPrint("🏰 " + state.getMessages().getBundle().get("mission.report.king.intro1"));
        slowPrinter.slowPrint("🧝‍♂️ " + state.getMessages().getBundle().get("mission.report.king.intro2"));
        slowPrinter.slowPrint("👑 " + state.getMessages().getBundle().get("mission.report.king.line1"));
        slowPrinter.slowPrint("🧝‍♂️ " + state.getMessages().getBundle().get("mission.report.king.line2"));
        slowPrinter.slowPrint("👑 " + state.getMessages().getBundle().get("mission.report.king.line3"));
        slowPrinter.slowPrint("🧝‍♂️ " + state.getMessages().getBundle().get("mission.report.king.line4"));
        slowPrinter.slowPrint("👑 " + state.getMessages().getBundle().get("mission.report.king.line5"));
        slowPrinter.slowPrint("👑 " + state.getMessages().getBundle().get("mission.report.king.line6"));
        slowPrinter.slowPrint("⚔️ " + state.getMessages().getBundle().get("mission.report.king.closure"));
    }
}

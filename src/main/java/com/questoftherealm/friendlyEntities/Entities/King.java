package com.questoftherealm.friendlyEntities.Entities;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.quests.RiseOfTheGoblinThreat;
import com.questoftherealm.friendlyEntities.NpcType;
import com.questoftherealm.friendlyEntities.Npc;
import com.questoftherealm.interaction.MissionInteractions;
import com.questoftherealm.interaction.SlowPrinter;
import com.questoftherealm.localization.MessageBundle;

public class King extends Npc {

    public King(String name) {
        super(NpcType.KING, name);
    }

    @Override
    public void talk(Player player, boolean isSimulation) {
        if(player.getCurQuest() instanceof RiseOfTheGoblinThreat q && !q.isReportedToKing()) {
            if (isSimulation) {
                q.setReportedToKing(true);
                return;
            }
            kingDialog();
            q.setReportedToKing(true);
        }
        else{
            System.out.println(MessageBundle.get("king.talked"));
        }
    }

    private void printMessage(String key) {
        String message = MessageBundle.get(key);
        message = message.replace("\\n", "\n");
        SlowPrinter.slowPrint(message);
    }

    private void kingDialog() {
        SlowPrinter.slowPrint("🏰 " + MessageBundle.get("mission.report.king.intro1"));
        SlowPrinter.slowPrint("🧝‍♂️ " + MessageBundle.get("mission.report.king.intro2"));
        SlowPrinter.slowPrint("👑 " + MessageBundle.get("mission.report.king.line1"));
        SlowPrinter.slowPrint("🧝‍♂️ " + MessageBundle.get("mission.report.king.line2"));
        SlowPrinter.slowPrint("👑 " + MessageBundle.get("mission.report.king.line3"));
        SlowPrinter.slowPrint("🧝‍♂️ " + MessageBundle.get("mission.report.king.line4"));
        SlowPrinter.slowPrint("👑 " + MessageBundle.get("mission.report.king.line5"));
        SlowPrinter.slowPrint("👑 " + MessageBundle.get("mission.report.king.line6"));
        SlowPrinter.slowPrint("⚔️ " + MessageBundle.get("mission.report.king.closure"));
    }
}

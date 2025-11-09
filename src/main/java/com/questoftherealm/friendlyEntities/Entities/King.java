package com.questoftherealm.friendlyEntities.Entities;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.friendlyEntities.NpcType;
import com.questoftherealm.friendlyEntities.Npc;
import com.questoftherealm.interaction.SlowPrinter;
import com.questoftherealm.localization.MessageBundle;

public class King extends Npc {
    public static boolean hasTalkedToTheKing = false;

    public King(String name) {
        super(NpcType.King, name);
    }

    @Override
    public void talk(Player player, boolean isSimulation) {
        if (isSimulation) {
            hasTalkedToTheKing = true;
            return;
        }

        printMessage("king.scene.intro");
        printMessage("king.scene.kneel");
        printMessage("king.scene.king.doubt");
        printMessage("king.scene.report");
        printMessage("king.scene.king.realization");
        printMessage("king.scene.warning");
        printMessage("king.scene.decision");
        printMessage("king.scene.end");

        hasTalkedToTheKing = true;
    }

    private void printMessage(String key) {
        String message = MessageBundle.get(key);
        // Convert escaped \n in properties file to real newlines
        message = message.replace("\\n", "\n");
        SlowPrinter.slowPrint(message);
    }

    @Override
    public boolean isHasTalked() {
        return false;
    }
}

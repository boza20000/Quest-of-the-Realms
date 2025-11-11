package com.questoftherealm.expeditions.missions;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.expeditions.quests.RiseOfTheGoblinThreat;

public class Assemble_an_Army extends Mission {
    public Assemble_an_Army(Player player) {
        super("Assemble an Army",
                "Rally knights, archers, and mages to face the goblins.",
                player,
                null
        );
    }
    public Assemble_an_Army(){}

    @Override
    public boolean checkCompletion() {
        if (isCompleted()) return true;
        if (player.getCurQuest() instanceof RiseOfTheGoblinThreat q &&
                q.isKnightsTriedToRecruit() && q.isArchersTriedToRecruit() && q.isMagesTriedToRecruit() && q.isReportedToKing()) {
            updateArmyStatus(q);
            complete();
            return true;
        }
        return false;
    }

    public void updateArmyStatus(RiseOfTheGoblinThreat q) {
        if (q.isKnightsRecruited()) {
            q.addArmyPower(15);
        }
        if (q.isMagesRecruited()) {
            q.addArmyPower(20);
        }
        if (q.isArchersRecruited()) {
            q.addArmyPower(20);
        }
    }
}

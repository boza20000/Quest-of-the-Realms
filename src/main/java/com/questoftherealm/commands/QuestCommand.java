package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.expeditions.QuestFactory;
import com.questoftherealm.game.Game;
import com.questoftherealm.map.Tile;

public class QuestCommand extends Command {

    public QuestCommand() {
        super("quest");
    }

    @Override
    public String getDescription() {
        return "gives you the cur quest with all its missions";
    }

    @Override
    public void execute(String[] args) {
        if (args.length > 2) {
            System.out.println("Usage: quest");
            return;
        }
        if (Game.getPlayer() == null) {
            System.out.println("Error: No player loaded.");
            return;
        }
        if (Game.getPlayer().getCurQuest() == null) {
            System.out.println("Error: No quest loaded.");
            return;
        }
        if (Game.getPlayer().getCurMission() == null) {
            System.out.println("Error: No mission loaded.");
            return;
        }

        for(Mission m: QuestFactory.getCurrentQuest().getMissions()){
            System.out.println(m.getTask());
        }
    }

}

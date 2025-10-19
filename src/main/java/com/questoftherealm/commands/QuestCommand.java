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
        return "quest — shows your active quest and its list of missions";
    }

    @Override
    public boolean makeSafe(String[] args, Player player) {
        if (args.length != 1) {
            System.out.println("Usage: " + getDescription());
            return false;
        }
        return playerBaseCheck(player);
    }

    @Override
    public void execute(String[] args) {
        Player player = Game.getPlayer();
        if(!makeSafe(args, player)){
            return;
        }

        if (player.getCurQuest() == null) {
            System.out.println("Error: No quest loaded.");
            return;
        }
        if (player.getCurMission() == null) {
            System.out.println("Error: No mission loaded.");
            return;
        }

        for (Mission m : QuestFactory.getCurrentQuest().getMissions()) {
            System.out.println(m.getTask());
        }
    }

}

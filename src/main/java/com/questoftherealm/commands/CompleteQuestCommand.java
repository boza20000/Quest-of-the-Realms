package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.game.Game;

import java.util.List;

public class CompleteQuestCommand extends Command {
    public CompleteQuestCommand() {
        super("progress");
    }

    @Override
    public void execute(String[] args) {
        Player player = Game.getPlayer();
        if(!makeSafe(args,player)){
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
        List<Mission> missions = List.of();
        if(player.getCurQuest()!=null) {
            try {
                missions = Game.getQuests().peek().getMissions();
            } catch (NullPointerException e) {
                e.getSuppressed();
                System.out.println("Missions unavailable they are null");
            }
        }
        if (!missions.isEmpty()) {
            for (Mission m : missions) {
                System.out.println(m.getTask() + " " + ((m.isCompleted()) ? "✔" : "❌"));
            }
        }
        else{
            System.out.println("No available quest");
        }
    }

    @Override
    public String getDescription() {
        return "progress — displays progress on your current quest and its missions";
    }

    @Override
    public boolean makeSafe(String[] args, Player player) {
        if (args.length != 1) {
            System.out.println("Usage: " + getDescription());
            return false;
        }
        return playerBaseCheck(player);
    }

}

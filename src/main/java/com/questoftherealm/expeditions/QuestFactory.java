package com.questoftherealm.expeditions;

import com.questoftherealm.expeditions.quests.*;
import com.questoftherealm.interaction.SlowPrinter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class QuestFactory {
    private static final Queue<Quest> quests = new LinkedList<>();

    public QuestFactory() {
        register(new StartQuest());
        register(new NorthExploration());
        register(new GoblinAmbush());
        register(new RiseOfTheGoblinThreat());
        register(new FinalBattle());
    }

    public static Queue<Quest> getQuests() {
        return quests;
    }

    public static Quest getCurrentQuest() {
        return quests.peek();
    }

    public static void nextQuest() {
        if(!quests.isEmpty() && quests.peek().isCompleted()){
            quests.poll();
        }
        else if (!quests.isEmpty() && !quests.peek().isCompleted()){
            SlowPrinter.slowPrint("Quest is not completed");
        }
        else {
            System.out.println("No more quests available");
        }
    }

    public static Mission getCurrentMission() {
        for (Mission m : getCurrentQuest().getMissions()) {
            if (!m.isCompleted()) {
              return m;
            }
        }
        return null;
    }

    void register(Quest quest) {
        quests.offer(quest);
    }

    public static void listAllQuests() {
        System.out.println("All quests:");
        quests.forEach((Quest quest) -> System.out.println("Quest:" + quest.getName()));
    }

}

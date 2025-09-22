package com.questoftherealm.expeditions;

import com.questoftherealm.expeditions.quests.*;

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
    public static Queue<Quest> getQuests(){
        return quests;
    }

    public static Quest getCurrentQuest(){
        return quests.peek();
    }


    void register(Quest quest) {
        quests.offer(quest);
    }

    public static void listAllQuests() {
        System.out.println("All quests:");
        quests.forEach((Quest quest) -> System.out.println("Quest:" + quest.getName()));
    }

}

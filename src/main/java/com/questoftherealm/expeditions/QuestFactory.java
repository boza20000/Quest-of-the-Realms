package com.questoftherealm.expeditions;

import java.util.HashMap;

public class QuestFactory {
    HashMap<String, Quests> quests = new HashMap<>();

    public QuestFactory() {
        register("Start Journey", new StartQuest());
        register("Explore the North", new NorthExploration());
        //..
    }


    void register(String name, Quests quest) {
        quests.put(name, quest);
    }

    public static void listAllCurQuests() {

    }

}

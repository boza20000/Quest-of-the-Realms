package com.questoftherealm.expeditions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.interfaces.MissionCondition;
import com.questoftherealm.expeditions.quests.*;
import com.questoftherealm.interaction.SlowPrinter;
import java.util.LinkedList;
import java.util.Queue;

public class QuestFactory {
    private final Queue<Quest> quests = new LinkedList<>();
    @JsonIgnore
    private Player player;

    private void registerDefaultQuests(Player player) {
        player.setQuestFactory(this);
        register(new StartQuest(player));
        register(new NorthExploration(player));
        register(new GoblinAmbush(player));
        register(new RiseOfTheGoblinThreat(player));
        register(new FinalBattle(player));
    }

    public QuestFactory(Player player) {
        this.player = player;
        registerDefaultQuests(player);
    }

    @JsonCreator
    public QuestFactory(@JsonProperty("quests") Queue<Quest> questList) {
        if (questList != null) this.quests.addAll(questList);
    }

    @JsonProperty("quests")
    public Queue<Quest> getQuests() {
        return quests;
    }


    @JsonIgnore
    public void setPlayer(Player player) {
        this.player = player;
        for (Quest quest : quests) {
            quest.setPlayer(player);
        }
    }
    @JsonIgnore
    public Quest getCurrentQuest() {
        return quests.peek();
    }

    public void nextQuest() {
        if (!quests.isEmpty() && quests.peek().isCompleted()) {
            quests.poll();
        } else if (!quests.isEmpty() && !quests.peek().isCompleted()) {
            SlowPrinter.slowPrint("Quest is not completed");
        } else {
            System.out.println("No more quests available");
        }
    }
    @JsonIgnore
    public Mission getCurrentMission() {
        if (getCurrentQuest() == null) {
            return null;
        }
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

    public void listAllQuests() {
        System.out.println("All quests:");
        quests.forEach((Quest quest) -> System.out.println("Quest:" + quest.getName()));
    }

    public void restoreAfterLoad(Player loadedPlayer) {
        this.player = loadedPlayer;
        for (Quest q : quests) {
            q.setPlayer(loadedPlayer);
            if (q.getMissions() != null) {
                for (Mission m : q.getMissions()) {
                    m.setPlayer(loadedPlayer);
                    MissionConditionType type = m.getConditionType();
                    if (type != null && type != MissionConditionType.CUSTOM_CHECK_COMPLETION) {
                        MissionCondition logic = MissionConditionFactory.getCondition(type);
                        m.setCondition(logic);
                    }
                }
            }
        }
    }
}

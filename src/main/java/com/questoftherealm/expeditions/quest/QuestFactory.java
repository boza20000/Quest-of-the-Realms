package com.questoftherealm.expeditions.quest;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.missions.Mission;
import com.questoftherealm.expeditions.quest.quests.*;
import com.questoftherealm.game.GameState;
import com.questoftherealm.interaction.SlowPrinter;

import java.util.LinkedList;
import java.util.Queue;

public class QuestFactory {
    private final Queue<Quest> quests = new LinkedList<>();
    @JsonIgnore
    private Player player;

    private void registerDefaultQuests(Player player, GameState state) {
        player.setQuestFactory(this);
        register(new StartQuest(player, state));
        register(new NorthExploration(player, state));
        register(new GoblinAmbush(player, state));
        register(new RiseOfTheGoblinThreat(player, state));
        register(new FinalBattle(player, state));
    }

    public QuestFactory(Player player, GameState state) {
        this.player = player;
        registerDefaultQuests(player, state);
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

    public void nextQuest(GameState state) {
        if (!quests.isEmpty() && quests.peek().isCompleted()) {
            quests.poll();
        } else if (!quests.isEmpty() && !quests.peek().isCompleted()) {
            SlowPrinter slowPrinter = new SlowPrinter(state);
            slowPrinter.slowPrint(state.getMessages().getBundle().get("quest.notReady"));
        } else {
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("quest.noMore"));
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

    public void restoreAfterLoad(Player loadedPlayer, GameState state) {
        this.player = loadedPlayer;
        for (Quest q : quests) {
            q.setPlayer(loadedPlayer);
            q.setState(state);
            if (q.getMissions() != null) {
                for (Mission m : q.getMissions()) {
                    m.setPlayer(loadedPlayer);
                    m.setState(state);
                }
            }
        }
    }
}

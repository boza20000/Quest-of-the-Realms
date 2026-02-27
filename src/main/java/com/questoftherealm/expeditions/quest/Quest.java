package com.questoftherealm.expeditions.quest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.missions.Mission;
import com.questoftherealm.expeditions.missions.MissionFactory;
import com.questoftherealm.expeditions.quest.quests.*;
import com.questoftherealm.game.GameState;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = StartQuest.class, name = "StartQuest"),
        @JsonSubTypes.Type(value = NorthExploration.class, name = "NorthExploration"),
        @JsonSubTypes.Type(value = GoblinAmbush.class, name = "GoblinAmbush"),
        @JsonSubTypes.Type(value = RiseOfTheGoblinThreat.class, name = "RiseOfTheGoblinThreat"),
        @JsonSubTypes.Type(value = FinalBattle.class, name = "FinalBattle")
})

public class Quest {
    private final List<Mission> missions;
    private final String name;
    private final String description;
    private boolean completed = false;
    @JsonIgnore
    private Player player;
    @JsonIgnore
    private GameState state;
    private QuestTypes questTypes;

    public Quest(QuestTypes type, Player player, GameState state) {
        this.state = state;
        this.player = player;
        this.missions = type.getMissions().stream()
                .map(m -> MissionFactory.createMission(m, player, state))
                .collect(Collectors.toList());
        this.description = type.getDescription(state);
        this.name = type.getName(state);
        this.questTypes = type;
    }
    protected Quest() {
        this.missions = List.of();
        this.name = null;
        this.description = null;
        this.questTypes = null;
    }

    public boolean isCompleted() {
        return completed;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Player getPlayer() {
        return player;
    }

    public List<Mission> getMissions() {
        return missions;
    }

    public QuestTypes getQuestTypes() {return questTypes;}

    public synchronized void updateStatus(GameState state) {
        boolean isAllReady = true;
        if (this.isCompleted()) return;
        for (Mission m : this.getMissions()) {
            m.checkCompletion();
            if (!m.isCompleted()) {
                isAllReady = false;
            }
        }
        if (isAllReady) {
            this.setCompleted(true);
            player.getQuestFactory().nextQuest(state);
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("quest.completed", name));
        }
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public void setQuestTypes(QuestTypes questTypes) {
        this.questTypes = questTypes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Quest q)) return false;
        return Objects.equals(this.getName(), q.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public synchronized void setPlayer(Player player) {
        this.player = player;
        for (Mission m : getMissions()) {
            m.setPlayer(player);
        }
    }
}

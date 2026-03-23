package com.questoftherealm.interaction;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.expeditions.missions.Mission;
import com.questoftherealm.expeditions.missions.Missions;
import com.questoftherealm.expeditions.quest.quests.RiseOfTheGoblinThreat;
import com.questoftherealm.game.GameServices;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.RandomService;
import com.questoftherealm.game.interfaces.Input;
import com.questoftherealm.game.interfaces.Output;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RecruitmentManagerTest {

    private GameState state;
    private Player player;
    private RiseOfTheGoblinThreat quest;
    private RecruitmentManager manager;
    
    // Mocks / Drivers
    private final Input input = mock(Input.class);
    private final Output output = mock(Output.class);
    private final RandomService randomService = mock(RandomService.class);
    private final Random random = mock(Random.class);
    
    private final Queue<String> inputs = new LinkedList<>();
    private final Queue<Integer> randoms = new LinkedList<>();

    @BeforeEach
    void setup() {
        GameServices services = new GameServices(output, input, randomService);
        state = new GameState("TestRealm", services);
        
        // Use real Player and Quest objects
        player = new Player("Hero", PlayerTypes.Warrior, state);
        player.addMoney(1000,state);
        
        quest = new RiseOfTheGoblinThreat(player, state);
        Mission mission = new Mission(Missions.ASSEMBLE_ARMY, player, state);
        
        player.setCurQuest(quest);
        player.setCurMission(mission);
        
        manager = new RecruitmentManager(state);

        // Wiring the mocks
        when(randomService.random()).thenReturn(random);
        when(input.nextLine()).thenAnswer(i -> inputs.poll());
        when(random.nextInt(anyInt())).thenAnswer(i -> randoms.isEmpty() ? 0 : randoms.poll());
        
        // Ensure RandomService works for helper roll(state) = nextInt(100)
        // The RecruitmentManager calls `state.getGameServices().getRandom().random().nextInt(100)`
    }

    private void given(Integer... rnd) { 
        for(Integer i : rnd) randoms.offer(i);
    }
    private void withInput(String... in) { 
        for(String s : in) inputs.offer(s);
    }

    @Test
    void knightsRecruitment() {
        // Success: Persuade < 50
        given(40); withInput("1");
        manager.talkToTheKnights(player, state);
        assertTrue(quest.isKnightsRecruited(), "Knights should be recruited by persuasion");
        
        // Reset
        quest.setKnightsRecruited(false);
        
        // Success: Fail Persuade -> Pay
        given(60); withInput("1", "1");
        manager.talkToTheKnights(player, state);
        assertTrue(quest.isKnightsRecruited(), "Knights should be recruited by payment");
    }

    @Test
    void archersRecruitment() {
        // Success: Eager < 35
        given(30);
        manager.talkToTheArchers(player, state);
        assertTrue(quest.isArchersRecruited(), "Archers should be recruited eagerly");

        // Reset
        quest.setArchersRecruited(false);

        // Success: Resource -> Pay
        given(40); withInput("2");
        manager.talkToTheArchers(player, state);
        assertTrue(quest.isArchersRecruited(), "Archers should be recruited by payment");
    }

    @Test
    void magesRecruitment() {
        // Success: Prophecy < 30
        given(20);
        manager.talkToTheMages(player, state);
        assertTrue(quest.isMagesRecruited(), "Mages should be recruited by prophecy");

        // Reset
        quest.setMagesRecruited(false);

        // Success: Price -> Pay
        given(50); withInput("1");
        manager.talkToTheMages(player, state);
        assertTrue(quest.isMagesRecruited(), "Mages should be recruited by payment");
    }
}

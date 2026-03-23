package com.questoftherealm.game;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.commands.Command;
import com.questoftherealm.commands.CommandFactory;
import com.questoftherealm.exceptions.InvalidCommand;
import com.questoftherealm.exceptions.SaveError;
import com.questoftherealm.game.interfaces.Input;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.interaction.ConsoleController;
import com.questoftherealm.localization.LocalizationService;
import com.questoftherealm.localization.MessageBundle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class GameLoopTest {

    private GameLoop gameLoop;
    private CommandFactory commandFactory;
    private Game game;
    private GameState gameState;
    private Input input;
    private Output output;
    private ConsoleController console;
    private Player player;
    private ServerClock serverClock;

    @BeforeEach
    void setUp() throws Exception {
        // Setup only essential mocks
        commandFactory = mock(CommandFactory.class);
        game = mock(Game.class);
        gameState = mock(GameState.class);
        input = mock(Input.class);
        output = mock(Output.class);
        console = mock(ConsoleController.class);
        player = mock(Player.class);
        serverClock = mock(ServerClock.class);

        GameServices gameServices = mockGameServices();
        LocalizationService localizationService = mockLocalization();

        when(game.getGameState()).thenReturn(gameState);
        when(game.getConsole()).thenReturn(console);
        when(gameState.getGameServices()).thenReturn(gameServices);
        when(gameState.getMessages()).thenReturn(localizationService);
        when(gameState.getPlayer("Player")).thenReturn(player);
        when(gameState.getClock()).thenReturn(serverClock);
        when(player.isActive()).thenReturn(true);
        when(player.isDead()).thenReturn(false);

        doReturn(false).when(gameState).isGameOver();
        doReturn(false).when(game).isRunning();

        gameLoop = new GameLoop();
        Field factory = GameLoop.class.getDeclaredField("factory");
        factory.setAccessible(true);
        factory.set(gameLoop, commandFactory);
    }

    private GameServices mockGameServices() {
        GameServices services = mock(GameServices.class);
        when(services.getInput()).thenReturn(input);
        when(services.getOutput()).thenReturn(output);
        return services;
    }

    private LocalizationService mockLocalization() {
        LocalizationService loc = mock(LocalizationService.class);
        MessageBundle bundle = mock(MessageBundle.class);
        when(bundle.get(anyString())).thenAnswer(inv -> inv.getArgument(0));
        when(loc.getBundle()).thenReturn(bundle);
        return loc;
    }

    @Test
    void givenGameLoop_whenStart_thenClockInitialized() {
        when(serverClock.now()).thenReturn(100L);

        gameLoop.startLoop(game, "Player");

        verify(serverClock).now();
        verify(player).setStartTime(100L);
    }

    @Test
    void givenGameLoop_whenEnd_thenPlayTimeTracked() {
        gameLoop.startLoop(game, "Player");

        verify(player).trackPlayTime(gameState);
    }

    @Test
    void givenGameOver_whenLoop_thenEndScreenShown() {
        when(game.isRunning()).thenReturn(true, false);
        when(gameState.isGameOver()).thenReturn(false, true);
        when(input.nextLine()).thenReturn("");

        gameLoop.startLoop(game, "Player");

        verify(console).displayEnd(player);
    }

    @Test
    void givenPlayerDeadAfterCommand_whenLoop_thenRespawn() throws InvalidCommand {
        when(game.isRunning()).thenReturn(true, true,false);
        when(input.nextLine()).thenReturn("attack");
        when(player.isDead()).thenReturn(false, true);
        
        Command cmd = mock(Command.class);
        when(commandFactory.getCommand("attack", gameState)).thenReturn(cmd);

        gameLoop.startLoop(game, "Player");

        verify(player).respawn(gameState);
    }

    @Test
    void givenValidCommand_whenExecute_thenQuestUpdated() throws InvalidCommand {
        when(game.isRunning()).thenReturn(true, false);
        when(input.nextLine()).thenReturn("look");
        
        Command cmd = mock(Command.class);
        when(commandFactory.getCommand("look", gameState)).thenReturn(cmd);

        gameLoop.startLoop(game, "Player");

        verify(player).updateQuestStatus(gameState);
        verify(output).println("gameLoop.command.success");
    }

    @Test
    void givenInvalidCommand_whenExecute_thenErrorShown() throws InvalidCommand {
        when(game.isRunning()).thenReturn(true, false);
        when(input.nextLine()).thenReturn("bad");
        when(commandFactory.getCommand("bad", gameState)).thenThrow(new InvalidCommand(""));

        gameLoop.startLoop(game, "Player");

        verify(output).println("error.command.InvalidCommand");
    }

    @Test
    void givenEmptyInput_whenLoop_thenSkipped() throws InvalidCommand {
        when(game.isRunning()).thenReturn(true, false);
        when(input.nextLine()).thenReturn("");

        gameLoop.startLoop(game, "Player");

        verify(commandFactory, never()).getCommand(anyString(), any());
    }

    @Test
    void givenSaveError_whenExecute_thenMessageShown() throws InvalidCommand {
        when(game.isRunning()).thenReturn(true, false);
        when(input.nextLine()).thenReturn("save");
        
        Command cmd = mock(Command.class);
        when(commandFactory.getCommand("save", gameState)).thenReturn(cmd);
        doThrow(new SaveError("Failed")).when(cmd).execute(any(), any(), any());

        gameLoop.startLoop(game, "Player");

        verify(output).println("Failed");
    }

    @Test
    void givenGameEnd_whenLoop_thenPlayerRemoved() {
        when(game.isRunning()).thenReturn(true, false);
        when(gameState.isGameOver()).thenReturn(false, true);
        when(input.nextLine()).thenReturn("");
        when(player.getName()).thenReturn("Player");

        gameLoop.startLoop(game, "Player");

        verify(gameState).removePlayer("Player");
    }
}







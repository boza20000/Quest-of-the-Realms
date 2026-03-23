package com.questoftherealm.client;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.game.GameServices;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.RandomService;
import com.questoftherealm.game.interfaces.Input;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.interaction.ConsoleController;
import com.questoftherealm.localization.LocalizationService;
import com.questoftherealm.localization.MessageBundle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ClientRequestHandlerTest {

    private Socket socket;
    private GameState gameState;
    private GameServices gameServices;
    private Input input;
    private Output output;
    private AtomicInteger counter;
    private Map<String, GameState> activeGames;
    private Map<String, Player> activePlayers;
    private LocalizationService localizationService;
    private MessageBundle bundle;

    @BeforeEach
    void setUp() {
        socket = mock(Socket.class);
        gameState = mock(GameState.class);
        gameServices = mock(GameServices.class);
        input = mock(Input.class);
        output = mock(Output.class);
        bundle = mock(MessageBundle.class);

        counter = new AtomicInteger(1);
        activeGames = new ConcurrentHashMap<>();
        activePlayers = new ConcurrentHashMap<>();
        localizationService = mock(LocalizationService.class);

        when(gameState.getGameServices()).thenReturn(gameServices);
        when(gameServices.getInput()).thenReturn(input);
        when(gameServices.getOutput()).thenReturn(output);
        when(gameServices.getRandom()).thenReturn(new RandomService());

        when(gameState.getMessages()).thenReturn(localizationService);
        when(localizationService.getBundle()).thenReturn(bundle);
        when(bundle.get(anyString())).thenReturn("Mock Message");
        when(bundle.get(anyString(), any())).thenReturn("Mock Message");
    }


    @Test
    void givenExistingRoom_whenJoinRoom_thenRoomIsJoined() throws Exception {
        String roomName = "Room1";
        GameState mockRoomState = mock(GameState.class);
        when(mockRoomState.isPrivate()).thenReturn(false);
        activeGames.put(roomName, mockRoomState);

        when(gameServices.getInput().nextLine()).thenReturn(roomName);

        ClientRequestHandler handler = new ClientRequestHandler(socket, counter, gameState, activeGames, activePlayers);
        Method method = ClientRequestHandler.class.getDeclaredMethod("roomChoice", Output.class, ConsoleController.class);
        method.setAccessible(true);

        ConsoleController console = mock(ConsoleController.class);
        String result = (String) method.invoke(handler, output, console);

        assertNotNull(result);
        assertEquals(roomName, result);

        Field roomField = ClientRequestHandler.class.getDeclaredField("serverRoom");
        roomField.setAccessible(true);
        assertEquals(roomName, roomField.get(handler));
    }

    @Test
    void givenPrivateRoom_whenJoinRoom_thenRetryAndJoinPublic() throws Exception {
        String privateRoom = "PrivateRoom";
        String publicRoom = "PublicRoom";

        GameState privateState = mock(GameState.class);
        when(privateState.isPrivate()).thenReturn(true);
        activeGames.put(privateRoom, privateState);

        GameState publicState = mock(GameState.class);
        when(publicState.isPrivate()).thenReturn(false);
        activeGames.put(publicRoom, publicState);

        when(gameServices.getInput().nextLine()).thenReturn(privateRoom).thenReturn(publicRoom);

        ClientRequestHandler handler = new ClientRequestHandler(socket, counter, gameState, activeGames, activePlayers);
        Method method = ClientRequestHandler.class.getDeclaredMethod("roomChoice", Output.class, ConsoleController.class);
        method.setAccessible(true);

        ConsoleController console = mock(ConsoleController.class);
        String result = (String) method.invoke(handler, output, console);

        assertEquals(publicRoom, result);
    }

    @Test
    void givenSinglePlayerRequest_whenJoinRoom_thenPrivateModeSet() throws Exception {
        String inputStr = "singleplayer";
        when(gameServices.getInput().nextLine()).thenReturn(inputStr);

        ClientRequestHandler handler = new ClientRequestHandler(socket, counter, gameState, activeGames, activePlayers);
        Method method = ClientRequestHandler.class.getDeclaredMethod("roomChoice", Output.class, ConsoleController.class);
        method.setAccessible(true);

        ConsoleController console = mock(ConsoleController.class);
        String result = (String) method.invoke(handler, output, console);

        assertNull(result);
        verify(gameState).setPrivate(true);
    }

    @Test
    void givenNewRoomName_whenJoinRoom_thenNewRoomCreated() throws Exception {
        String newRoom = "NewAdventureRoom";
        when(gameServices.getInput().nextLine()).thenReturn(newRoom);

        ClientRequestHandler handler = new ClientRequestHandler(socket, counter, gameState, activeGames, activePlayers);
        Method method = ClientRequestHandler.class.getDeclaredMethod("roomChoice", Output.class, ConsoleController.class);
        method.setAccessible(true);

        ConsoleController console = mock(ConsoleController.class);
        String result = (String) method.invoke(handler, output, console);

        assertEquals(newRoom, result);

        Field creatingField = ClientRequestHandler.class.getDeclaredField("creatingNewRoom");
        creatingField.setAccessible(true);
        assertTrue((boolean) creatingField.get(handler));

        Field roomField = ClientRequestHandler.class.getDeclaredField("serverRoom");
        roomField.setAccessible(true);
        assertEquals(newRoom, roomField.get(handler));
    }

    @Test
    void givenRunningClient_whenExceptionOccurs_thenSocketIsClosed() {
        when(gameState.getGameServices()).thenThrow(new RuntimeException("Simulated Failure"));

        ClientRequestHandler handler = new ClientRequestHandler(
                socket, counter, gameState, activeGames, activePlayers
        );

        try {
            handler.run();
        } catch (RuntimeException e) {
        }

        try {
            verify(socket).close();
        } catch (Exception e) {
        }
    }
}

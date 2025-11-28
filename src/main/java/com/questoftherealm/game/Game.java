package com.questoftherealm.game;

import com.questoftherealm.characters.player.WeaponFactory;
import com.questoftherealm.exceptions.FileNotLoaded;
import com.questoftherealm.exceptions.IntroException;
import com.questoftherealm.exceptions.OutputServiceError;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.interaction.Console;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.interaction.MissionInteractions;
import com.questoftherealm.items.ItemRegistry;

import java.io.IOException;

public class Game {
    private GameState gameState;
    private Console console;
    private Output output;


    public GameState getGameState() {
        return gameState;
    }

    public void newGame() {
        Player player = buildPlayerCharacter();
        gameState.setPlayer(player);

        try {
            console.showIntro();
        } catch (IOException e) {
            throw new IntroException("Intro was corrupted");
        }
        console.worldIntro();

    }

    public void loadGame(GameState state) {
        LoadGame loadGame = new LoadGame();
        output.println(state.getMessages().getBundle().get("game.load.ask"));
        loadGame.printSaves(state);
        String save = state.getGameServices().getInput().nextLine();
        output.println(state.getMessages().getBundle().get("game.load.loading"));
        try {
            loadGame.loadGameSave(save, state);
        } catch (Exception e) {
            throw new FileNotLoaded(state.getMessages().getBundle().get("game.load.error"));
        }
    }

    public Player buildPlayerCharacter() {
        String name = console.characterCreationScreen(gameState);
        int count = 0;
        int typeChoice;

        while (true) {
            try {
                output.print(gameState.getMessages().getBundle().get("game.choice.prompt"));
                typeChoice = Integer.parseInt(gameState.getGameServices().getInput().nextLine());
                if (typeChoice >= 1 && typeChoice <= 4) break;
                else {
                    count++;
                    if (count <= 1) output.println(gameState.getMessages().getBundle().get("game.choice.invalidRange"));
                }
            } catch (NumberFormatException e) {
                count++;
                if (count <= 1) output.println(gameState.getMessages().getBundle().get("game.choice.invalidInput"));
            }
        }

        PlayerTypes type = PlayerTypes.fromInt(typeChoice, gameState);
        Player player = new Player(name, type, gameState);

        output.println(gameState.getMessages().getBundle().get("game.player.choice.confirm", type));
        output.println(gameState.getMessages().getBundle().get("game.player.character.show", player.getPlayerCharacter().stats(gameState)));

        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            output.println(gameState.getMessages().getBundle().get("game.sleep.error"));
            throw new RuntimeException(e);
        }
        return player;
    }

    private Output gameType(int gameType) throws IOException {
        return switch (gameType) {
            case 1 -> new ConsoleOutput();
//            case 2 -> {
//                Socket socket = MultiplayerClient.connectToServer();
//                yield new ClientSocketOutput(socket);
//            }
            default -> new ConsoleOutput();
        };
    }

    public void start() {
        //single or multiplayer
        int gameRules = gameRules(new InputService(), new ConsoleOutput());
        try {
            output = gameType(gameRules);
        } catch (IOException e) {
            throw new OutputServiceError("output was not loaded");
        }
        gameState = new GameState(null, new GameServices(output));
        console = new Console(gameState);
        console.displayTitle();

        // new or load game
        int gameMode = getGameMode();
        try {
            switch (gameMode) {
                case 1 -> newGame();
                case 2 -> loadGame(gameState);
            }
        }
        catch (IntroException e){
            output.println("Intro was corrupted");
            return;
        }
        catch (FileNotLoaded e){
            output.println(gameState.getMessages().getBundle().get("game.load.error"));
            return;
        }

        try {
            NpcInitializer npcInitializer = new NpcInitializer();
            npcInitializer.registerAll(gameState);
        } catch (Exception e) {
            output.println(gameState.getMessages().getBundle().get("game.npc.init.error"));
            return;
        }

        GameLoop loop = new GameLoop();
        loop.startLoop(this);
    }

    private int getGameMode() {
        int count = 0;
        int type;
        while (true) {
            try {
                count++;
                type = console.showMainMenu(count, gameState);
                if (type == 1 || type == 2) break;
            } catch (NumberFormatException e) {
                if (count <= 1) output.println(gameState.getMessages().getBundle().get("game.start.invalidInput"));
                count++;
            }
        }
        return type;
    }

    public int gameRules(InputService inputService, ConsoleOutput outputService) {
        outputService.print("1.Single player\n2.Multiplayer\n");
        outputService.print("Enter choice >");
        int mode;
        int count = 1;
        while (true) {
            try {
                mode = Integer.parseInt(inputService.nextLine());
                if (mode == 1 || mode == 2) {
                    break;
                } else if (count <= 1) {
                    outputService.println("Number out of range! single player (1) multiplayer (2)");
                }
            } catch (NumberFormatException e) {
                outputService.println("Enter number!");
            }
            count++;
            outputService.print(">");
            if (count >= 10) {
                outputService.print("Default choice -> single player mode");
                return 1;
            }
        }
        return mode;
    }

    public Console getConsole() {
        return console;
    }
}

package com.questoftherealm.game;

import com.questoftherealm.exceptions.FileNotLoaded;
import com.questoftherealm.exceptions.IntroException;
import com.questoftherealm.exceptions.NpcInitializationFailed;
import com.questoftherealm.exceptions.OutputServiceError;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.interaction.Console;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.localization.MessageBundle;

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
            throw new IntroException(gameState.getMessages().getBundle().get("game.new.error.introCorrupted"));
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

        sleep();
        return player;
    }

    private void sleep() {
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            output.println(gameState.getMessages().getBundle().get("game.sleep.error"));
            throw new RuntimeException(e);
        }
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
            throw new OutputServiceError(gameState.getMessages().getBundle().get("game.error.outputNotLoaded"));
        }
        gameState = new GameState(null, new GameServices(output));
        console = new Console(gameState);
        console.displayTitle();

        try {
            initializeGame();
        } catch (FileNotLoaded | NpcInitializationFailed | IntroException e) {
            output.println(e.getMessage());
            return;
        }

        GameLoop loop = new GameLoop();
        loop.startLoop(this);
    }

    private void initializeGame() {
        int gameMode = getGameMode();
        switch (gameMode) {
            case 1 -> newGame();
            case 2 -> loadGame(gameState);
        }
        NpcInitializer npcInitializer = new NpcInitializer();
        npcInitializer.registerAll(gameState);
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
        MessageBundle temp = new MessageBundle();
        outputService.print(temp.get("game.rules.options"));
        outputService.print(temp.get("game.rules.prompt"));
        int mode;
        int count = 1;
        while (true) {
            try {
                mode = Integer.parseInt(inputService.nextLine());
                if (mode == 1 || mode == 2) {
                    break;
                } else if (count <= 1) {
                    outputService.println(temp.get("game.rules.invalidRange"));
                }
            } catch (NumberFormatException e) {
                outputService.println(temp.get("game.rules.invalidInput"));
            }
            count++;
            outputService.print(temp.get("game.rules.prompt"));
            if (count >= 10) {
                outputService.print(temp.get("game.rules.defaultChoice"));
                return 1;
            }
        }
        return mode;
    }

    public Console getConsole() {
        return console;
    }
}
package com.questoftherealm.interaction;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.exceptions.ArtException;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.interfaces.Input;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.localization.ArtLocalization;
import com.questoftherealm.localization.MessageBundle;
import com.questoftherealm.server.ServerLogger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.questoftherealm.game.GameConstants.RED;
import static com.questoftherealm.game.GameConstants.RESET;


public class ConsoleController {
    private final GameState state;
    private final SlowPrinter slowPrinter;
    private final ArtLocalization artLocalization;
    private final MessageBundle bundle;

    public ConsoleController(GameState state) {
        this.state = state;
        this.slowPrinter = new SlowPrinter(state);
        this.bundle = state.getMessages().getBundle();
        try {
            this.artLocalization = new ArtLocalization(state);
        } catch (FileNotFoundException e) {
            throw new ArtException(e.getMessage());
        }
    }

    private Output output() {
        return state.getGameServices().getOutput();
    }

    private Input input() {
        return state.getGameServices().getInput();
    }

    public void displayTitle() {
        output().println();
        output().println();
        output().println();
        output().println();
        try {
            output().println(artLocalization.getArt("quest_of_the_realms"));
        } catch (IOException e) {
            throw new ArtException(e.getMessage());
        }
    }

    public void worldIntro() {
        slowPrinter.slowPrint(bundle.get("console.worldIntro"));
        input().nextLine();
        output().println();
    }

    public void displayPlayTime(Player player) {
        int h = player.getPlayTime().hours();
        int m = player.getPlayTime().minutes();
        String playTimeArt;
        try {
            playTimeArt = String.format(artLocalization.getArt("time_played"), h, m);
        } catch (IOException e) {
            ServerLogger.get().error("ConsoleController: IOException displaying play time art", e);
            throw new ArtException(e.getMessage());
        }
        output().println(playTimeArt);
    }


    public void displayEnd(Player player) {
        output().println();
        output().println();
        output().println();
        output().println();
        try {
            output().println(artLocalization.getArt("game_over"));
        } catch (IOException e) {
            throw new ArtException(e.getMessage());
        }

        displayPlayTime(player);
    }

    public void showIntro() throws IOException {
        Story story = new Story();
        final int delay = 30;
        int count = 0;
        output().println();
        output().println(bundle.get("console.intro.skip", RED, RESET));

        for (char c : story.getStory(state).toCharArray()) {
            output().print(String.valueOf(c));
            output().flush();
            count++;
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }

            if (input().available() > 0) {
                while (input().available() > 0) {
                    input().read();
                }
                output().print(story.getStory(state).substring(count));
                output().flush();
                break;
            }
        }
        input().read();
    }

    public int showMainMenu(int count) {
        if (count <= 1) {
            try {
                output().println(artLocalization.getArt("start_menu"));
            } catch (IOException e) {
                throw new ArtException(e.getMessage());
            }
            printSymbol();
        } else {
            printSymbol();
        }
        
        while (true) {
            String input = input().nextLine().trim();

            if (input.isBlank()) {
                if (count <= 1) {
                    output().println(bundle.get("game.choice.invalidInput.menu.blank"));
                }
                printSymbol();
                count++;
                continue;
            }
            
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                if (count <= 1) {
                    output().println(bundle.get("game.choice.invalidInput.menu"));
                }
                printSymbol();
                count++;
            }
        }
    }

    public String usernameCreationScreen(Map<String, Player> activePlayer) {
        output().println(bundle.get("console.charCreate.namePrompt"));
        printSymbol();
        String name = input().nextLine();
        int count = 0;
        while (name.isBlank() || activePlayer.containsKey(name)) {
            if (activePlayer.containsKey(name)) {
                output().println(bundle.get("console.charCreate.nameUsedError"));
            }
            if (count < 1 && name.isBlank()) {
                output().println(bundle.get("console.charCreate.nameEmptyError"));
            }
            printSymbol();
            name = input().nextLine();
            count++;
        }
        return name;
    }

    public void displayCharacterOptions() {
        output().println(bundle.get("console.charCreate.classPrompt"));
        output().println(bundle.get("console.charCreate.class1"));
        output().println(bundle.get("console.charCreate.class2"));
        output().println(bundle.get("console.charCreate.class3"));
        output().println(bundle.get("console.charCreate.class4"));
    }

    private void printSymbol() {
        output().print(bundle.get("console.menu.prompt"));
        output().flush();
    }

    public PlayerTypes characterCreationScreen(Output output, ConsoleController console, GameState state) {
        console.displayCharacterOptions();
        int count = 0;
        int typeChoice;

        while (true) {
            try {
                output.print(state.getMessages().getBundle().get("console.menu.prompt"));
                output.flush();
                String inputLine = state.getGameServices().getInput().nextLine().trim();

                if (inputLine.isBlank()) {
                    count++;
                    if (count <= 1) {
                        output.println(state.getMessages().getBundle().get("game.choice.invalidInput"));
                    }
                    continue;
                }
                
                typeChoice = Integer.parseInt(inputLine);
                if (typeChoice >= 1 && typeChoice <= 4) break;
                else {
                    count++;
                    if (count <= 1)
                        output.println(state.getMessages().getBundle().get("game.choice.invalidRange"));
                }
            } catch (NumberFormatException e) {
                ServerLogger.get().warn("ConsoleController: Invalid character class selection input", e);
                count++;
                if (count <= 1) output.println(state.getMessages().getBundle().get("game.choice.invalidInput"));
            }
        }

        return PlayerTypes.fromInt(typeChoice, state);
    }

    public void printRoomsOptions(Output out, Map<String, GameState> activeGames) {
        List<String> liveRooms = activeGames.entrySet().stream()
                .filter(e -> !e.getValue().isGameOver() && !e.getValue().getActivePlayers().isEmpty())
                .map(Map.Entry::getKey)
                .toList();

        if (liveRooms.isEmpty()) {
            out.println(bundle.get("client.handler.room.noActive"));
            out.print(bundle.get("client.handler.room.choice"));
            out.flush();
            return;
        }
        for (String roomName : liveRooms) {
            int playerCount = activeGames.get(roomName).getActivePlayers().size();
            out.println(bundle.get("client.handler.room.listEntry", roomName, playerCount, playerCount != 1 ? "s" : ""));
        }
        out.println(bundle.get("client.handler.room.prompt"));
        out.print(bundle.get("client.handler.room.choice"));
        out.flush();
    }
}
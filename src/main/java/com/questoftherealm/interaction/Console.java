package com.questoftherealm.interaction;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.InputService;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.localization.ArtLocalization;
import com.questoftherealm.localization.MessageBundle;
import java.io.FileNotFoundException;
import java.io.IOException;
import static com.questoftherealm.game.GameConstants.RED;
import static com.questoftherealm.game.GameConstants.RESET;


public class Console {
    private final GameState state;
    private final Output output;
    private final InputService input;
    private final SlowPrinter slowPrinter;
    private final ArtLocalization artLocalization;
    private final MessageBundle bundle;

    public Console(GameState state) {
        this.state = state;
        this.output = state.getGameServices().getOutput();
        this.input = state.getGameServices().getInput();
        this.slowPrinter = new SlowPrinter(state);
        this.bundle = state.getMessages().getBundle();
        try {
            this.artLocalization = new ArtLocalization(state);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void displayTitle() {
        output.println();
        output.println();
        output.println();
        output.println();
        try {
            output.println(artLocalization.getArt("quest_of_the_realms"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void worldIntro() {
        slowPrinter.slowPrint(bundle.get("console.worldIntro"));
        state.getGameServices().getInput().nextLine();
        output.println();
    }

    public void displayPlayTime(Player player) {
        int h = player.getPlayTime().hours();
        int m = player.getPlayTime().minutes();
        String playTimeArt;
        try {
            playTimeArt = String.format(artLocalization.getArt("time_played"), h, m);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        output.println(playTimeArt);
    }


    public void displayEnd(Player player) {
        output.println();
        output.println();
        output.println();
        output.println();
        try {
            output.println(artLocalization.getArt("game_over"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        displayPlayTime(player);
    }

    public void showIntro() throws IOException {
        Story story = new Story();
        final int delay = 30;
        int count = 0;
        output.println();
        output.println(bundle.get("console.intro.skip", RED, RESET));

        for (char c : story.getStory(state).toCharArray()) {
            output.print(String.valueOf(c));
            count++;
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }

            if (input.available() > 0) {
                while (input.available() > 0) {
                    input.read();
                }
                output.print(story.getStory(state).substring(count));
                break;
            }
        }
        input.read();
    }

    public int showMainMenu(int count, GameState state) {
        if (count <= 1) {
            try {
                output.println(artLocalization.getArt("start_menu"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            printSymbol();
        } else {
            printSymbol();
        }
        return Integer.parseInt(state.getGameServices().getInput().nextLine());
    }

    public String characterCreationScreen(GameState state) {
        output.println();
        output.println(bundle.get("console.charCreate.namePrompt"));
        printSymbol();
        String name = state.getGameServices().getInput().nextLine();
        int count = 0;
        while (name.isBlank()) {
            if (count < 1) {
                output.println(bundle.get("console.charCreate.nameEmptyError"));
            }
            printSymbol();
            name = state.getGameServices().getInput().nextLine();
            count++;
        }
        displayCharacterOptions();
        return name;
    }

    private void displayCharacterOptions() {
        output.println(bundle.get("console.charCreate.classPrompt"));
        output.println(bundle.get("console.charCreate.class1"));
        output.println(bundle.get("console.charCreate.class2"));
        output.println(bundle.get("console.charCreate.class3"));
        output.println(bundle.get("console.charCreate.class4"));
    }

    private void printSymbol(){
        output.print(bundle.get("console.menu.prompt"));
    }
}
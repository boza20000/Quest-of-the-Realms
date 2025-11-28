package com.questoftherealm.interaction;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.InputService;
import com.questoftherealm.game.interfaces.Output;

import java.io.IOException;

import static com.questoftherealm.game.GameConstants.RED;
import static com.questoftherealm.game.GameConstants.RESET;


public class Console {
    private final GameState state;
    private final Output output;
    private final InputService input;
    private final SlowPrinter slowPrinter;

    public Console(GameState state) {
        this.state = state;
        this.output = state.getGameServices().getOutput();
        this.input = state.getGameServices().getInput();
        this.slowPrinter = new SlowPrinter(state);
    }

    public void displayTitle() {
        output.println();
        output.println();
        output.println();
        output.println();
        output.println("""
                                                              ░██████   ░██     ░██ ░██████████   ░██████   ░██████████     ░██████   ░██████████   ░██████████░██     ░██ ░██████████\s
                                                             ░██   ░██  ░██     ░██ ░██          ░██   ░██      ░██        ░██   ░██  ░██               ░██    ░██     ░██ ░██        \s
                                                            ░██     ░██ ░██     ░██ ░██         ░██             ░██       ░██     ░██ ░██               ░██    ░██     ░██ ░██        \s
                                                            ░██     ░██ ░██     ░██ ░█████████   ░████████      ░██       ░██     ░██ ░█████████        ░██    ░██████████ ░█████████ \s
                                                            ░██     ░██ ░██     ░██ ░██                 ░██     ░██       ░██     ░██ ░██               ░██    ░██     ░██ ░██        \s
                                                             ░██   ░██   ░██   ░██  ░██          ░██   ░██      ░██        ░██   ░██  ░██               ░██    ░██     ░██ ░██        \s
                                                              ░██████     ░██████   ░██████████   ░██████       ░██         ░██████   ░██               ░██    ░██     ░██ ░██████████\s
                                                                   ░██                                                                                                                \s
                                                                    ░██                                                                                                               \s
                                                                                                                                                                        \s
                                                                                  ░█████████  ░██████████    ░███    ░██         ░███     ░███   ░██████                                        \s
                                                                                  ░██     ░██ ░██           ░██░██   ░██         ░████   ░████  ░██   ░██                                       \s
                                                                                  ░██     ░██ ░██          ░██  ░██  ░██         ░██░██ ░██░██ ░██                                              \s
                                                                                  ░█████████  ░█████████  ░█████████ ░██         ░██ ░████ ░██  ░████████                                       \s
                                                                                  ░██   ░██   ░██         ░██    ░██ ░██         ░██  ░██  ░██         ░██                                      \s
                                                                                  ░██    ░██  ░██         ░██    ░██ ░██         ░██       ░██  ░██   ░██                                       \s
                                                                                  ░██     ░██ ░██████████ ░██    ░██ ░██████████ ░██       ░██   ░██████                                        \s
                                                                                                                                          \s
                                                                                                                                          \s
                                                                                                                                                     \s
                """);
    }

    public void worldIntro() {
        slowPrinter.slowPrint(state.getMessages().getBundle().get("console.worldIntro"));
        state.getGameServices().getInput().nextLine();
        output.println();
    }

    public void displayPlayTime(Player player) {
        int h = player.getPlayTime().hours();
        int m = player.getPlayTime().minutes();

        String playTimeArt = String.format("""             
                                                                                                    ┏━┓╻  ┏━┓╻ ╻   ╺┳╸╻┏┳┓┏━╸   \s
                                                                                                    ┣━┛┃  ┣━┫┗┳┛    ┃ ┃┃┃┃┣╸  ╺━╸  %02d hours %02d minutes
                                                                                                    ╹  ┗━╸╹ ╹ ╹     ╹ ╹╹ ╹┗━╸
                """, h, m);
        output.println(playTimeArt);
    }


    public void displayEnd(Player player) {
        output.println();
        output.println();
        output.println();
        output.println();
        output.println("""
                                                                █████████    █████████   ██████   ██████ ██████████       ███████    █████   █████ ██████████ ███████████ \s
                                                               ███░░░░░███  ███░░░░░███ ░░██████ ██████ ░░███░░░░░█     ███░░░░░███ ░░███   ░░███ ░░███░░░░░█░░███░░░░░███\s
                                                              ███     ░░░  ░███    ░███  ░███░█████░███  ░███  █ ░     ███     ░░███ ░███    ░███  ░███  █ ░  ░███    ░███\s
                                                             ░███          ░███████████  ░███░░███ ░███  ░██████      ░███      ░███ ░███    ░███  ░██████    ░██████████ \s
                                                             ░███    █████ ░███░░░░░███  ░███ ░░░  ░███  ░███░░█      ░███      ░███ ░░███   ███   ░███░░█    ░███░░░░░███\s
                                                             ░░███  ░░███  ░███    ░███  ░███      ░███  ░███ ░   █   ░░███     ███   ░░░█████░    ░███ ░   █ ░███    ░███\s
                                                              ░░█████████  █████   █████ █████     █████ ██████████    ░░░███████░      ░░███      ██████████ █████   █████
                                                               ░░░░░░░░░  ░░░░░   ░░░░░ ░░░░░     ░░░░░ ░░░░░░░░░░       ░░░░░░░         ░░░      ░░░░░░░░░░ ░░░░░   ░░░░░\s
                                                                                                                                                                          \s
                                                                                                                                                                          \s
                                                                                                                                                                          \s
                """);

        displayPlayTime(player);
    }

    public void showIntro() throws IOException {
        Story story = new Story();
        final int delay = 30;
        int count = 0;
        output.println();
        output.println("(Press " + RED + "Enter" + RESET + " to skip the story)");

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
            output.println(
                    """
                            ╺┓     ┏┓╻┏━╸╻ ╻   ┏━╸┏━┓┏┳┓┏━╸  \s
                             ┃     ┃┗┫┣╸ ┃╻┃   ┃╺┓┣━┫┃┃┃┣╸   \s
                            ╺┻╸╹   ╹ ╹┗━╸┗┻┛   ┗━┛╹ ╹╹ ╹┗━╸  \s
                            ┏━┓    ╻  ┏━┓┏━┓╺┳┓   ┏━╸┏━┓┏┳┓┏━╸
                            ┏━┛    ┃  ┃ ┃┣━┫ ┃┃   ┃╺┓┣━┫┃┃┃┣╸\s
                            ┗━╸╹   ┗━╸┗━┛╹ ╹╺┻┛   ┗━┛╹ ╹╹ ╹┗━╸
                                                             \s
                            """);
            output.print(">");
        } else {
            output.print(">");
        }
        return Integer.parseInt(state.getGameServices().getInput().nextLine());
    }

    public String characterCreationScreen(GameState state) {
        output.println();
        output.println("Choose your name: ");
        output.print(">");
        String name = state.getGameServices().getInput().nextLine();
        int count = 0;
        while (name.isBlank()) {
            if (count < 1) {
                output.println("Name can't be empty");
            }
            output.print(">");
            name = state.getGameServices().getInput().nextLine();
            count++;
        }
        output.println("Choose your character:");
        output.println("1. Warrior — A strong fighter with high health and defense.");
        output.println("2. Mage — A master of spells, fragile but devastating.");
        output.println("3. Orc — Brutal and tough, with raw strength and resilience.");
        output.println("4. Rogue — Quick and cunning, excels at stealth and critical strikes.");
        return name;
    }

    private void displayModes() {
    }
}

package com.questoftherealm.interaction;

import com.questoftherealm.game.GameConstants;

import java.io.IOException;
import java.util.Scanner;

import static com.questoftherealm.game.GameConstants.RED;
import static com.questoftherealm.game.GameConstants.RESET;

public class GameUI {
    private final Story story = new Story();
    private final Scanner scanner = new Scanner(System.in);
    private final Console console = new Console();

    public void showIntro() throws IOException {
        final int delay = 30;
        int count = 0;
        System.out.println();
        System.out.println("(Press " + RED + "Enter" + RESET + " to skip the story)");

        for (char c : story.getStory().toCharArray()) {
            System.out.print(c);
            count++;
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }

            if (System.in.available() > 0) {
                while (System.in.available() > 0) {
                    System.in.read();
                }
                System.out.print(story.getStory().substring(count));
                break;
            }
        }
        System.in.read();
    }

    public int showMainMenu(int count) {
        if (count <= 1) {
            System.out.println(
                    """
                            ╺┓     ┏┓╻┏━╸╻ ╻   ┏━╸┏━┓┏┳┓┏━╸  \s
                             ┃     ┃┗┫┣╸ ┃╻┃   ┃╺┓┣━┫┃┃┃┣╸   \s
                            ╺┻╸╹   ╹ ╹┗━╸┗┻┛   ┗━┛╹ ╹╹ ╹┗━╸  \s
                            ┏━┓    ╻  ┏━┓┏━┓╺┳┓   ┏━╸┏━┓┏┳┓┏━╸
                            ┏━┛    ┃  ┃ ┃┣━┫ ┃┃   ┃╺┓┣━┫┃┃┃┣╸\s
                            ┗━╸╹   ┗━╸┗━┛╹ ╹╺┻┛   ┗━┛╹ ╹╹ ╹┗━╸
                                                             \s
                            """);
            System.out.print(">");
        } else {
            System.out.print(">");
        }
        return Integer.parseInt(scanner.nextLine());
    }

    public String characterCreationScreen() {
        System.out.println();
        System.out.println("Choose your name: ");
        System.out.print(">");
        String name = getScanner().nextLine();
        int count = 0;
        while (name.isBlank()) {
            if (count < 1) {
                System.out.println("Name can't be empty");
            }
            System.out.print(">");
            name = getScanner().nextLine();
            count++;
        }
        System.out.println("Choose your character:");
        System.out.println("1. Warrior — A strong fighter with high health and defense.");
        System.out.println("2. Mage — A master of spells, fragile but devastating.");
        System.out.println("3. Orc — Brutal and tough, with raw strength and resilience.");
        System.out.println("4. Rogue — Quick and cunning, excels at stealth and critical strikes.");
        return name;
    }


    public Scanner getScanner() {
        return scanner;
    }

    public Console getConsole() {
        return console;
    }
}

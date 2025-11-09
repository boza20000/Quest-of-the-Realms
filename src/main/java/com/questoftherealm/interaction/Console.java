package com.questoftherealm.interaction;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.localization.MessageBundle;

import java.util.Scanner;


public class Console {
    private static final Scanner scanner = new Scanner(System.in);

    public void clear() {
        System.out.println();
    }

    public void displayTitle() {
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("""
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
        SlowPrinter.slowPrint(MessageBundle.get("console.worldIntro"));
        scanner.nextLine();
        System.out.println();
    }

    public void displayPlayTime(Player player) {
        int h = player.getPlayTime().hours();
        int m = player.getPlayTime().minutes();

        String playTimeArt = String.format("""             
                                                                                                    ┏━┓╻  ┏━┓╻ ╻   ╺┳╸╻┏┳┓┏━╸   \s
                                                                                                    ┣━┛┃  ┣━┫┗┳┛    ┃ ┃┃┃┃┣╸  ╺━╸  %02d hours %02d minutes
                                                                                                    ╹  ┗━╸╹ ╹ ╹     ╹ ╹╹ ╹┗━╸
                """,h,m);
        System.out.println(playTimeArt);
    }


    public void displayEnd(Player player) {
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("""
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


}

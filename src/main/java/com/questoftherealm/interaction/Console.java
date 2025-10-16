package com.questoftherealm.interaction;

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
        SlowPrinter.slowPrint("""
                🌍 You stand before the gates of the king’s castle, summoned by the ruler himself.
                The king entrusts you with the most important task of your life: to protect the kingdom from an unknown threat.
                The castle’s banners flutter in the wind as courtiers and guards watch silently, aware of the gravity of your mission.
                A sense of destiny weighs upon you, the fate of the realm now rests in your hands.
                \n(Press ENTER to continue...)
                """);
        scanner.nextLine();
        System.out.println();
    }


    public void displayEnd(){
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
    }


}

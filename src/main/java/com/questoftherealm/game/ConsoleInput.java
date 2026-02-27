package com.questoftherealm.game;

import com.questoftherealm.game.interfaces.Input;

import java.io.IOException;
import java.util.Scanner;

public class ConsoleInput implements Input {
    private final Scanner scanner = new Scanner(System.in);


    public String nextLine(){
        return scanner.nextLine();
    }

    public int nextInt(){
        return Integer.parseInt(scanner.nextLine());
    }

    public int available() {
        try {
            return System.in.available();
        } catch (IOException e) {
            return 0;
        }
    }

    public int read()  {
        try {
            return System.in.read();
        } catch (IOException e) {
            return -1;
        }
    }
}

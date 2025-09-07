package com.questoftherealm.game;

import com.questoftherealm.game.Commands.Command;

import java.util.Scanner;

import static com.questoftherealm.game.Console.printErrorInline;
import static com.questoftherealm.game.Game.gameOver;

public class GameLoop {
    private final Scanner scanner = new Scanner(System.in);
    private final CommandFactory factory = new CommandFactory();

    public void startLoop() {
        while (!gameOver) {
            System.out.print(">");
            String command = scanner.nextLine().trim();
            if(command.isEmpty()){
                continue;
            }
            String[] parts = command.split("\\\\s+");
            String commandName = parts[0];
            Command cmd = factory.getCommand(commandName);
            if(cmd==null){
                System.out.println("No such command exists");
                try {
                    Thread.sleep(1500);
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
                printErrorInline(cmd);
            }
            else{
                cmd.execute(parts);
            }
        }
    }

}

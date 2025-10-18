package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.game.Game;
import com.questoftherealm.game.GameConstants;
import com.questoftherealm.interaction.Interactions;
import com.questoftherealm.interaction.SlowPrinter;
import com.questoftherealm.map.TileTypes;

public class MoveCommand extends Command {

    public MoveCommand() {
        super("move");
    }

    @Override
    public String getDescription() {
        return "move [north|south|east|west](direction)";
    }

    @Override
    public void execute(String[] args) {
        if (args.length > 2) {
            System.out.println("Usage: move <north|south|east|west>");
            return;
        }
        if (Game.getPlayer() == null) {
            System.out.println("Error: No player currently active.");
            return;
        }
        if(Game.getGameMap().curZone(Game.getPlayer().getX(), Game.getPlayer().getY()) == null){
            System.out.println("You are in an undefined area.");
            return;
        }

        String direction = args[1].toLowerCase();
        Player player = Game.getPlayer();
        int x = player.getX();
        int y = player.getY();

        switch (direction) {
            case "north" -> {
                if (y - 1 >= GameConstants.MAP_START) {
                    y -= 1;
                } else {
                    SlowPrinter.slowPrint("You can't go further north!");
                    return;
                }
            }
            case "south" -> {
                if (y + 1 < GameConstants.MAP_HEIGHT) {
                    y += 1;
                } else {
                    SlowPrinter.slowPrint("You can't go further south!");
                    return;
                }
            }
            case "east" -> {
                if (x + 1 < GameConstants.MAP_HEIGHT) {
                    x += 1;
                } else {
                    SlowPrinter.slowPrint("You can't go further east!");
                    return;
                }
            }
            case "west" -> {
                if (x - 1 >= GameConstants.MAP_START) {
                    x -= 1;
                } else {
                    SlowPrinter.slowPrint("You can't go further west!");
                    return;
                }
            }
            default -> {
                SlowPrinter.slowPrint("Invalid direction! Use north, south, east, or west.");
                return;
            }
        }

        TileTypes start = Game.getGameMap().curZone(player.getX(), player.getY()).getType();
        pathToDestination(direction, player);
        player.move(x, y);
        TileTypes end = Game.getGameMap().curZone(player.getX(), player.getY()).getType();
        if(Game.getGameMap().curZone(Game.getPlayer().getX(), Game.getPlayer().getY()) == null){
            System.out.println("You going to an undefined area.");
            return;
        }
        SlowPrinter.slowPrint(Interactions.getTransition(start, end));
        SlowPrinter.slowPrint("You have entered %s zone".formatted(end.toString().toLowerCase()));
    }


    private void pathToDestination(String direction, Player player) {
        try {
            System.out.print("Walking");
            for (int i = 0; i < 3; i++) {
                Thread.sleep(600);
                System.out.print(".");
            }
            Interactions.pathInteraction(Game.getGameMap().curZone(player.getX(), player.getY()).getType(), direction);
        } catch (Exception e) {
            System.out.println("Walking failed");
        }

    }

}

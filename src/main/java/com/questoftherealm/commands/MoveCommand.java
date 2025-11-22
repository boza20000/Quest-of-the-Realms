package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.game.Game;
import com.questoftherealm.game.GameConstants;
import com.questoftherealm.game.GameState;
import com.questoftherealm.interaction.SlowPrinter;
import com.questoftherealm.interaction.TravelManger;
import com.questoftherealm.map.TileTypes;

public class MoveCommand extends Command {

    public MoveCommand() {
        super("move");
    }

    @Override
    public String getDescription() {
        return "move [north|south|east|west] — move your character in the specified direction";
    }

    @Override
    public boolean makeSafe(String[] args, Player player, GameState state) {
        if (args.length != 2) {
            state.getGameServices().getOutput().println("Usage: " + getDescription());
            return false;
        }
        return playerBaseCheck(player, state);
    }

    @Override
    public void execute(String[] args, Player player, GameState state) {
        TravelManger travelManger = new TravelManger(state);
        SlowPrinter slowPrinter = new SlowPrinter(state);
        if (!makeSafe(args, player, state)) {
            return;
        }
        if (state.getMap().curZone(player.getX(), player.getY()) == null) {
            state.getGameServices().getOutput().println("You are in an undefined area.");
            return;
        }

        String direction = args[1].toLowerCase();
        int x = player.getX();
        int y = player.getY();

        switch (direction) {
            case "north" -> {
                if (y - 1 >= GameConstants.MAP_START) {
                    y -= 1;
                } else {
                    slowPrinter.slowPrint("You can't go further north!");
                    return;
                }
            }
            case "south" -> {
                if (y + 1 < GameConstants.MAP_END) {
                    y += 1;
                } else {
                    slowPrinter.slowPrint("You can't go further south!");
                    return;
                }
            }
            case "east" -> {
                if (x + 1 < GameConstants.MAP_END) {
                    x += 1;
                } else {
                    slowPrinter.slowPrint("You can't go further east!");
                    return;
                }
            }
            case "west" -> {
                if (x - 1 >= GameConstants.MAP_START) {
                    x -= 1;
                } else {
                    slowPrinter.slowPrint("You can't go further west!");
                    return;
                }
            }
            default -> {
                slowPrinter.slowPrint("Invalid direction! Use north, south, east, or west.");
                return;
            }
        }
        if (!state.isSimulation()) {
            player.move(x, y);
            state.getMap().movePlayer(player, player.getX(), player.getY());
            return;
        }
        TileTypes start = state.getMap().curZone(player.getX(), player.getY()).getType();
        pathToDestination(direction, player, state, travelManger);
        player.move(x, y);
        state.getMap().movePlayer(player, player.getX(), player.getY());
        if (state.getMap().curZone(player.getX(), player.getY()) == null) {
            state.getGameServices().getOutput().println("You going to an undefined area.");
            return;
        }
        TileTypes end = state.getMap().curZone(player.getX(), player.getY()).getType();
        slowPrinter.slowPrint(travelManger.getTransition(start, end));
        slowPrinter.slowPrint("You have entered %s zone".formatted(end.toString().toUpperCase()));
    }


    private void pathToDestination(String direction, Player player, GameState state, TravelManger travelManger) {
        try {
            state.getGameServices().getOutput().print("Walking");
            for (int i = 0; i < 3; i++) {
                Thread.sleep(600);
                state.getGameServices().getOutput().print(".");
            }
            travelManger.pathInteraction(state.getMap().curZone(player.getX(), player.getY()).getType(), direction, player, state);
        } catch (Exception e) {
            state.getGameServices().getOutput().println("Walking failed");
        }

    }

}

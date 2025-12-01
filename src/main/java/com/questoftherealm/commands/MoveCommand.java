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
    public String getDescription(GameState state) {
        return state.getMessages().getBundle().get("move.description");
    }

    @Override
    public boolean makeSafe(String[] args, Player player, GameState state) {
        if (args.length != 2) {
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("move.usage", getDescription(state)));
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
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("move.error.undefinedArea"));
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
                    slowPrinter.slowPrint(state.getMessages().getBundle().get("move.error.furtherNorth"));
                    return;
                }
            }
            case "south" -> {
                if (y + 1 < GameConstants.MAP_END) {
                    y += 1;
                } else {
                    slowPrinter.slowPrint(state.getMessages().getBundle().get("move.error.furtherSouth"));
                    return;
                }
            }
            case "east" -> {
                if (x + 1 < GameConstants.MAP_END) {
                    x += 1;
                } else {
                    slowPrinter.slowPrint(state.getMessages().getBundle().get("move.error.furtherEast"));
                    return;
                }
            }
            case "west" -> {
                if (x - 1 >= GameConstants.MAP_START) {
                    x -= 1;
                } else {
                    slowPrinter.slowPrint(state.getMessages().getBundle().get("move.error.furtherWest"));
                    return;
                }
            }
            default -> {
                slowPrinter.slowPrint(state.getMessages().getBundle().get("move.error.invalidDirection"));
                return;
            }
        }
        if (state.isSimulation()) {
            player.move(x, y);
            state.getMap().movePlayer(player, player.getX(), player.getY());
            return;
        }
        TileTypes start = state.getMap().curZone(player.getX(), player.getY()).getType();
        pathToDestination(direction, player, state, travelManger);
        player.move(x, y);
        state.getMap().movePlayer(player, player.getX(), player.getY());
        if (state.getMap().curZone(player.getX(), player.getY()) == null) {
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("move.info.undefinedDestination"));
            return;
        }
        TileTypes end = state.getMap().curZone(player.getX(), player.getY()).getType();
        slowPrinter.slowPrint(travelManger.getTransition(start, end));
        slowPrinter.slowPrint(state.getMessages().getBundle().get("move.info.enteredZone", end.toString().toUpperCase()));
    }


    private void pathToDestination(String direction, Player player, GameState state, TravelManger travelManger) {
        try {
            state.getGameServices().getOutput().print(state.getMessages().getBundle().get("move.info.walking"));
            for (int i = 0; i < 3; i++) {
                Thread.sleep(600);
                state.getGameServices().getOutput().print(state.getMessages().getBundle().get("move.info.dot"));
            }
            travelManger.pathInteraction(state.getMap().curZone(player.getX(), player.getY()).getType(), direction, player, state);
        } catch (Exception e) {
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("move.error.walkingFailed"));
        }

    }

}
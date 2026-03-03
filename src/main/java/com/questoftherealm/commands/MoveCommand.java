package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.game.GameConstants;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.Position;
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
        Position current = new Position(player.getX(), player.getY());
        Position next = handleMoving(direction, current.x(), current.y(), slowPrinter, state);

        if (next.equals(current)) {
            return;
        }

        if (state.isSimulation()) {
            handleSimulation(state, player, next.x(), next.y());
            return;
        }

        TileTypes start = state.getMap().curZone(player.getX(), player.getY()).getType();
        handleStartTile(player, state, direction, next.x(), next.y(), travelManger);

        if (state.getMap().curZone(player.getX(), player.getY()) == null) {
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("move.info.undefinedDestination"));
            return;
        }

        TileTypes end = state.getMap().curZone(player.getX(), player.getY()).getType();
        handleEndTile(player, state, start, travelManger, end, slowPrinter);

    }

    private Position handleMoving(String direction, int x, int y, SlowPrinter slowPrinter, GameState state) {
        switch (direction) {
            case "north" -> {
                if (y - 1 >= GameConstants.MAP_START) {
                    y -= 1;
                } else {
                    slowPrinter.slowPrint(state.getMessages().getBundle().get("move.error.furtherNorth"));
                }
            }
            case "south" -> {
                if (y + 1 < GameConstants.MAP_END) {
                    y += 1;
                } else {
                    slowPrinter.slowPrint(state.getMessages().getBundle().get("move.error.furtherSouth"));
                }
            }
            case "east" -> {
                if (x + 1 < GameConstants.MAP_END) {
                    x += 1;
                } else {
                    slowPrinter.slowPrint(state.getMessages().getBundle().get("move.error.furtherEast"));
                }
            }
            case "west" -> {
                if (x - 1 >= GameConstants.MAP_START) {
                    x -= 1;
                } else {
                    slowPrinter.slowPrint(state.getMessages().getBundle().get("move.error.furtherWest"));
                }
            }
            default -> slowPrinter.slowPrint(state.getMessages().getBundle().get("move.error.invalidDirection"));
        }
        return new Position(x, y);
    }

    private void handleEndTile(Player player, GameState state, TileTypes start, TravelManger travelManger, TileTypes end, SlowPrinter slowPrinter) {
        slowPrinter.slowPrint(travelManger.getTransition(start, end));
        slowPrinter.slowPrint(state.getMessages().getBundle().get("move.info.enteredZone", end.toString().toUpperCase()));
        increasePlayerManaPerMove(player);
    }

    private void handleStartTile(Player player, GameState state, String direction, int x, int y, TravelManger travelManger) {
        pathToDestination(direction, player, state, travelManger);
        player.move(x, y);
        state.getMap().movePlayer(player, player.getX(), player.getY());
    }

    private void handleSimulation(GameState state, Player player, int x, int y) {
        player.move(x, y);
        state.getMap().movePlayer(player, player.getX(), player.getY());
        increasePlayerManaPerMove(player);
    }


    private void pathToDestination(String direction, Player player, GameState state, TravelManger travelManger) {
        try {
            state.getGameServices().getOutput().print(state.getMessages().getBundle().get("move.info.walking"));
            state.getGameServices().getOutput().flush();
            for (int i = 0; i < 3; i++) {
                Thread.sleep(600);
                state.getGameServices().getOutput().print(state.getMessages().getBundle().get("move.info.dot"));
                state.getGameServices().getOutput().flush();
            }
            travelManger.pathInteraction(state.getMap().curZone(player.getX(), player.getY()).getType(), direction, player, state);
        } catch (Exception e) {
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("move.error.walkingFailed"));
        }

    }

    private void increasePlayerManaPerMove(Player player) {
        int manaPerMove = GameConstants.MANA_PER_MOVE;
        synchronized (player.getPlayerCharacter()) {
            player.getPlayerCharacter().setMana(player.getPlayerCharacter().getMana() + manaPerMove);
        }
    }

}
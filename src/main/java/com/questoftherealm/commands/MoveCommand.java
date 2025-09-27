package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.game.Game;
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
        String direction = args[1].toLowerCase();
        Player player = Game.getPlayer();
        int x = player.getX();
        int y = player.getY();
        switch (direction) {
            case "north" -> y -= 1;
            case "south" -> y += 1;
            case "east" -> x += 1;
            case "west" -> x -= 1;
        }
        TileTypes start = Game.getGameMap().curZone(player.getX(), player.getY()).getType();
        pathToDestination(direction,player);
        player.move(x, y);
        TileTypes end = Game.getGameMap().curZone(player.getX(), player.getY()).getType();
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
            Interactions.pathInteraction(Game.getGameMap().curZone(player.getX(), player.getY()).getType(),direction);
        } catch (Exception e) {
            System.out.println("Walking failed");
        }

    }

}

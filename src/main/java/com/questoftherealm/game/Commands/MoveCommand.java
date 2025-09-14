package com.questoftherealm.game.Commands;

import com.questoftherealm.game.Game;

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
        int x = Game.getPlayer().getX();
        int y = Game.getPlayer().getY();
        switch (direction) {
            case "north" -> x -= 1;
            case "south" -> x += 1;
            case "east" -> y += 1;
            case "west" -> y -= 1;
        }
        Game.getPlayer().move(x, y);
    }
}

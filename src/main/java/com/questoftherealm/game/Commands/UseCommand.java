package com.questoftherealm.game.Commands;

import com.questoftherealm.characters.Characters;
import com.questoftherealm.game.Game;

public class UseCommand extends Command {

    public UseCommand() {
        super("use");
    }

    @Override
    public String getDescription() {
        return "use [item]";
    }

    @Override
    public void execute(String[] args) {
        Characters curCharacter = Game.getPlayer().getPlayerCharacter();
        //curCharacter.useItem();
    }
}

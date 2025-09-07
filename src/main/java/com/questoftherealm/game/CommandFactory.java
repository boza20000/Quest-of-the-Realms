package com.questoftherealm.game;

import com.questoftherealm.game.Commands.*;


import java.util.HashMap;
import java.util.Map;

public class CommandFactory {
    private final Map<String, Command> commands = new HashMap<>();

    public CommandFactory() {
        register("help", new HelpCommand(this));
        register("move", new MoveCommand());
        register("look", new LookCommand());
        register("attack", new AttackCommand());
        register("use", new UseCommand());
        register("inventory", new InventoryCommand());
        register("equip", new EquipCommand());
        register("quests", new QuestsCommand());
        register("complete", new CompleteQuestCommand());
        register("save", new SaveCommand());
        register("load", new LoadCommand());
        register("exit", new ExitCommand());
    }

    private void register(String name, Command command) {
        commands.put(name, command);
    }

    public Command getCommand(String name) {
        return commands.get(name);
    }

    public Map<String, Command> getAllCommands() {
        return commands;
    }
}

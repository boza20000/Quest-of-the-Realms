package com.questoftherealm.commands;


import com.questoftherealm.exceptions.InvalidCommand;
import com.questoftherealm.game.GameState;

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
        register("quest", new QuestCommand());
        register("progress", new CompleteQuestCommand());
        register("save", new SaveCommand());
        register("load", new LoadCommand());
        register("exit", new ExitCommand());
        register("map", new MapCommand());
        register("take", new TakeCommand());
        register("explore", new ExploreCommand());
        register("stats", new StatsCommand());
        register("talk", new TalkCommand());
        register("super", new AbilityCommand());
    }

    private void register(String name, Command command) {
        commands.put(name, command);
    }

    public Command getCommand(String name, GameState state) {
        if (commands.get(name) == null) {
            throw new InvalidCommand(state.getMessages().getBundle().get("command.not.recognised"));
        }
        return commands.get(name);
    }

    public Map<String, Command> getAllCommands() {
        return commands;
    }
}

package com.questoftherealm.commands;

public class HelpCommand extends Command {
    private final CommandFactory factory;

    public HelpCommand(CommandFactory factory) {
        super("help");
        this.factory = factory;
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public void execute(String[] args) {
        if (args.length > 1) {
            System.out.println("Usage: [help]");
            return;
        }
        System.out.println("Available commands:");
        factory.getAllCommands().forEach((name, cmd) ->
                System.out.println("- " + name + "->" + cmd.getDescription())
        );
    }
}


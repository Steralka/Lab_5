package org.example.commands;

import org.example.managers.CommandManager;
import org.example.utility.Console;

/**
 * Команда 'help'. Выводит справку по доступным командам.
 */
public class Help extends Command {
    private final Console console;
    private final CommandManager commandManager;

    public Help(Console console, CommandManager commandManager) {
        super("help", "вывести справку по доступным командам");
        this.console = console;
        this.commandManager = commandManager;
    }

    /**
     * Выполняет команду при заданных аргументах.
     * @param args аргументы команды
     * @return Успешность выполнения команды
     */
    @Override
    public boolean apply(String[] args) {
        if (args.length > 0) {
            console.println("Неверное количество аргументов");
            console.println("Использование: '" + getName() + "'");
            return false;
        }

        commandManager.getCommands()
                .values()
                .forEach(command -> console.printTable(command.getName(), command.getDescription()));
        return true;
    }
}

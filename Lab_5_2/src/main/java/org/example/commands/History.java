package org.example.commands;

import org.example.managers.CommandManager;
import org.example.utility.Console;

import java.util.List;

/**
 * Команда 'history'. Выводит историю команд.
 */
public class History extends Command {
    private static final int COUNT_DISPLAY_COMMAND = 15;
    private final Console console;
    private final CommandManager commandManager;

    public History(Console console, CommandManager commandManager) {
        super("history", "выводит историю команд");
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
            console.println("Неправильное количество аргументов");
            console.println("Использование: '" + getName() + "'");
            return false;
        }

        List<String> commandHistory = commandManager.getCommandHistory();
        int displaySize = Math.min(commandHistory.size(), COUNT_DISPLAY_COMMAND);
        if (displaySize == 0) {
            console.println("История команд пуста");
            return true;
        }

        console.println("Последние " + displaySize + " команд:");
        for (int i = commandHistory.size() - 1; i >= commandHistory.size() - displaySize; i--) {
            console.println(commandHistory.get(i));
        }
        return true;
    }
}

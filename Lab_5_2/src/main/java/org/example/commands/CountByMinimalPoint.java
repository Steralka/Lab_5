package org.example.commands;

import org.example.managers.CollectionManager;
import org.example.utility.Console;

/**
 * Команда 'count_by_minimal_point'. Выводит количество элементов, значение поля {@code minimalPoint} которых равно заданному.
 */
public class CountByMinimalPoint extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public CountByMinimalPoint(Console console, CollectionManager collectionManager) {
        super("count_by_minimal_point minimalPoint", "вывести количество элементов, значение поля " +
                "minimalPoint которых равно заданному");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду при заданных аргументах.
     * @param args аргументы команды
     * @return Успешность выполнения команды
     */
    @Override
    public boolean apply(String[] args) {
        if (args.length > 1) {
            console.println("Неверное количество аргументов");
            console.println("Использование: '" + getName() + "'");
            return false;
        }

        Double targetMinimalPoint;
        try {
            targetMinimalPoint = args.length == 0 ? null : Double.parseDouble(args[0]);
        } catch (NumberFormatException | NullPointerException e) {
            console.println("Аргумент '" + args[0] + "' не является типом Double");
            return false;
        }

        console.println(collectionManager.countEqualMinimalPoint(targetMinimalPoint));
        return true;
    }
}

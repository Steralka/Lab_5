package org.example.commands;

import org.example.managers.CollectionManager;
import org.example.models.Difficulty;
import org.example.utility.Console;

import java.util.List;

/**
 * Команда 'print_field_descending_difficulty'. Выводит значение поля {@code difficulty} всех элементов в порядке убывания.
 */
public class PrintFieldDescendingDifficulty extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public PrintFieldDescendingDifficulty(Console console, CollectionManager collectionManager) {
        super("print_field_descending_difficulty", "вывести значение поля difficulty всех элементов " +
                "в порядке убывания");
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
        if (args.length > 0) {
            console.println("Неверное количество аргументов");
            console.println("использование: '" + getName() + "'");
            return false;
        }

        List<Difficulty> list = collectionManager.getFieldDescendingDifficulty();
        if (list.isEmpty()) {
            console.println("Список пуст");
        } else {
            console.println("Результат:");
            for (Difficulty difficulty : list) {
                console.println(difficulty);
            }
        }
        return true;
    }
}

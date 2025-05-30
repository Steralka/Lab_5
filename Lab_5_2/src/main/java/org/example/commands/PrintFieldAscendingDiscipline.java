package org.example.commands;

import org.example.managers.CollectionManager;
import org.example.models.Discipline;
import org.example.utility.Console;

import java.util.List;

/**
 * Команда 'print_field_ascending_discipline'. Выводит значение поля {@code discipline} всех элементов в порядке возрастания.
 */
public class PrintFieldAscendingDiscipline extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public PrintFieldAscendingDiscipline(Console console, CollectionManager collectionManager) {
        super("print_field_ascending_discipline", "вывести значение поля discipline всех элементов " +
                "в порядке возрастания");
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

        List<Discipline> list = collectionManager.getFieldAscendingDiscipline();
        if (list.isEmpty()) {
            console.println("Список пуст");
        } else {
            console.println("Результат:");
            for (Discipline discipline : list) {
                console.println(discipline);
            }
        }
        return true;
    }
}

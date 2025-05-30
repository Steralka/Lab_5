package org.example.commands;

import org.example.managers.CollectionManager;
import org.example.models.Ask;
import org.example.models.LabWork;
import org.example.utility.Console;

import java.util.Objects;

/**
 * Команда 'add'. Добавляет новый элемент в коллекцию.
 */
public class Add extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public Add(Console console, CollectionManager collectionManager) {
        super("add {element}", "добавить новый элемент в коллекцию");
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
            console.println("Использование: '" + getName() + "'");
            return false;
        }

        console.println("* Создание... ");
        LabWork newLab = Ask.askLabWork(console, collectionManager.getFreeId());
        if (Objects.isNull(newLab)) {
            console.println("Непредвиденная ошибка при создании элемента");
            return false;
        }

        collectionManager.add(newLab);
        console.println("Новый элемент успешно добавлен");
        return true;
    }
}

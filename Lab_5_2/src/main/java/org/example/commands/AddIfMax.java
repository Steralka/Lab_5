package org.example.commands;

import org.example.managers.CollectionManager;
import org.example.models.Ask;
import org.example.models.LabWork;
import org.example.utility.Console;

import java.util.Objects;

/**
 * Команда 'add_if_max'. Добавляет новый элемент в коллекцию, если его значение превышает значение
 * наибольшего элемента этой коллекции.
 */
public class AddIfMax extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public AddIfMax(Console console, CollectionManager collectionManager) {
        super("add_if_max {element}", "добавить новый элемент в коллекцию, если его значение " +
                "превышает значение наибольшего элемента этой коллекции");
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

        if (collectionManager.isMaxElement(newLab)) {
            collectionManager.add(newLab);
            console.println("Элемент добавлен");
        } else {
            console.println("Элемент не добавлен, поскольку он не является максимальным");
        }
        return true;
    }
}

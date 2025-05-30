package org.example.commands;

import org.example.managers.CollectionManager;
import org.example.models.Ask;
import org.example.models.LabWork;
import org.example.utility.Console;

import java.util.Objects;

/**
 * Команда 'remove_lower'. Удаляет из коллекции все элементы, меньшие, чем заданный.
 */
public class RemoveLower extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public RemoveLower(Console console, CollectionManager collectionManager) {
        super("remove_lower {element}", "удалить из коллекции все элементы, меньшие, чем заданный");
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
        LabWork targetLab = Ask.askLabWork(console, collectionManager.getFreeId());
        if (Objects.isNull(targetLab)) {
            console.println("Непредвиденная ошибка при создании элемента");
            return false;
        }

        int sizeBefore = collectionManager.size();
        collectionManager.removeLower(targetLab);
        int sizeAfter = collectionManager.size();

        console.println("Удалено " + (sizeBefore - sizeAfter) + " элементов");
        return true;
    }
}

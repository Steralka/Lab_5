package org.example.commands;

import org.example.managers.CollectionManager;
import org.example.utility.Console;

/**
 * Команда 'remove_by_id'. Удаляет элемент из коллекции по {@code id}.
 */
public class RemoveById extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public RemoveById(Console console, CollectionManager collectionManager) {
        super("remove_by_id", "удалить элемент из коллекции по id");
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
        if (args.length != 1) {
            console.println("Неверное количество аргументов");
            console.println("Использование: '" + getName() + "'");
            return false;
        }

        long id;
        try {
            id = Long.parseLong(args[0]);
        } catch (NumberFormatException e) {
            console.println("Аргумент '" + args[0] + "' не является типом Long");
            return false;
        }

        if (!collectionManager.contains(id)) {
            console.println("Элемента с id = " + id + " не найдено");
            return false;
        }

        collectionManager.remove(id);
        console.println("Элемент с id = " + id + " успешно удалён");
        return true;
    }
}

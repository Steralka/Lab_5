package org.example.commands;

import org.example.managers.CollectionManager;
import org.example.models.Ask;
import org.example.models.LabWork;
import org.example.utility.Console;

import java.util.Objects;

/**
 * Команда 'update'. Обновляет значение элемента по {@code id}.
 */
public class Update extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public Update(Console console, CollectionManager collectionManager) {
        super("update {id}", "обновить значение элемента по id");
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

        console.println("* Создание ... ");
        LabWork newLab = Ask.askLabWork(console, id);
        if (Objects.isNull(newLab)) {
            console.println("Непредвиденная ошибка при создании элемента");
            return false;
        }

        collectionManager.remove(id);
        collectionManager.add(newLab);

        console.println("Элемент с id = " + id + " успешно обновлён");
        return true;
    }
}


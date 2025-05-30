package org.example.commands;

import org.example.managers.CollectionManager;
import org.example.utility.Console;

import java.time.LocalDateTime;

/**
 * Команда 'info'. Выводит информацию о коллекции.
 */
public class Info extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public Info(Console console, CollectionManager collectionManager) {
        super("info", "вывести информацию о коллекции");
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

        LocalDateTime lastSaveTime = collectionManager.getLastSaveTime();
        String lastSaveTimeString = (lastSaveTime == null) ? "в данной сессии ещё не происходило сохранения" :
                lastSaveTime.toLocalDate().toString() + " " + lastSaveTime.toLocalTime().toString();

        LocalDateTime lastInitTime = collectionManager.getLastInitTime();
        String lastInitTimeString = (lastInitTime == null) ? "в данной сессии ещё не происходило инициализации" :
                lastInitTime.toLocalDate().toString() + " " + lastInitTime.toLocalTime().toString();

        console.println("Сведения о коллекции:");
        console.println("Тип: " + collectionManager.getCollection().getClass());
        console.println("Количество элементов: " + collectionManager.size());
        console.println("Дата последнего сохранения: " + lastSaveTimeString);
        console.println("Дата последней инициализации: " + lastInitTimeString);
        return true;
    }
}

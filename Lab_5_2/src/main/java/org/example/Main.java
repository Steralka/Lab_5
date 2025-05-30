package org.example;

import org.example.commands.*;
import org.example.managers.CSVParser;
import org.example.managers.CollectionManager;
import org.example.managers.CommandManager;
import org.example.utility.Console;
import org.example.utility.Runner;
import org.example.utility.StandardConsole;

public class Main {
    public static void main(String[] args) {
        Console console = new StandardConsole();

        if (args.length == 0) {
            console.printError("Введите имя загружаемого файла без расширения как аргумент командной строки. " +
                    "Повторите попытку");
            return;
        }

        String fileName = String.join("", args);
        CSVParser csvParser = new CSVParser(fileName, console);
        CollectionManager collectionManager = new CollectionManager(csvParser);

        console.println("* Загрузка значений из файла...");
        if (!collectionManager.loadCollection()) {
            console.printError("Загружаемый файл имеет проблемы с уникальностью id элементов");
            return;
        } else {
            console.println("Загрузка успешно завершена");
        }

        CommandManager commandManager = new CommandManager() {{
            register("help", new Help(console, this));
            register("info", new Info(console, collectionManager));
            register("show", new Show(console, collectionManager));
            register("add", new Add(console, collectionManager));
            register("update", new Update(console, collectionManager));
            register("remove_by_id", new RemoveById(console, collectionManager));
            register("clear", new Clear(console, collectionManager));
            register("save", new Save(console, collectionManager));
            register("execute_script", new ExecuteScript(console));
            register("exit", new Exit(console));
            register("add_if_max", new AddIfMax(console, collectionManager));
            register("remove_lower", new RemoveLower(console, collectionManager));
            register("history", new History(console, this));
            register("count_by_minimal_point", new CountByMinimalPoint(console, collectionManager));
            register("print_field_ascending_discipline", new PrintFieldAscendingDiscipline(console, collectionManager));
            register("print_field_descending_difficulty", new PrintFieldDescendingDifficulty(console, collectionManager));
        }};

        new Runner(console, commandManager).interactiveMode();
    }
}
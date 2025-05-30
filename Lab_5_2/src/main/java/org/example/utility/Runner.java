package org.example.utility;

import org.example.commands.Command;
import org.example.managers.CommandManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.*;

/**
 * Класс, реализующий действия программы.
 */
public class Runner {
    private static final int MAX_RECURSIVE_DEPTH = 2;
    private final Console console;
    private final CommandManager commandManager;
    private int scriptRecursiveDepth;

    public Runner(Console console, CommandManager commandManager) {
        this.console = console;
        this.commandManager = commandManager;
        this.scriptRecursiveDepth = 0;
    }

    /**
     * Коды возврата команд.
     */
    public enum ExitCode {
        OK, ERROR, RECURSIVE_ERROR, EXIT
    }

    /**
     * Интерактивный режим
     */
    public void interactiveMode() {
        ExitCode commandStatus;
        String[] userCommand;
        try {
            do {
                console.prompt();
                userCommand = console.readln().trim().split(" ");
                commandStatus = launchCommand(userCommand);
            } while (commandStatus != ExitCode.EXIT);
        } catch (NoSuchElementException e) {
            console.printError("Входная строка отсутствует");

            commandStatus = ExitCode.ERROR;
        } catch (IllegalStateException e) {
            console.printError("Непредвиденная ошибка");
            System.exit(0);
        }
    }


    /**
     * Режим для запуска скрипта.
     *
     * @param fileName полное имя файла
     * @return код завершения
     */
    public ExitCode scriptMode(String fileName) {
        if (++scriptRecursiveDepth == MAX_RECURSIVE_DEPTH) {
            console.println("Достигнута максимальная глубина рекурсии (" + MAX_RECURSIVE_DEPTH + "). " +
                    "Принудительное завершение скрипта.");
            return ExitCode.RECURSIVE_ERROR;
        }

        String[] userCommand;
        ExitCode commandStatus;

        try {
            Path path = Path.of(fileName);
            if (Files.notExists(path)) {
                console.println("Файл не существует");
                return ExitCode.ERROR;
            }
            if (!Files.isReadable(path)) {
                console.println("Прав для чтения нет");
                return ExitCode.ERROR;
            }
        } catch (InvalidPathException e) {
            console.println(fileName + " является некорректным путём");
            return ExitCode.ERROR;
        } catch (SecurityException e) {
            console.printError(e.getMessage());
            return ExitCode.ERROR;
        }

        try (Scanner scriptScanner = new Scanner(new File(fileName))) {
            console.selectFileScanner(scriptScanner);

            commandStatus = ExitCode.OK;
            while (commandStatus == ExitCode.OK && console.canReadln()) {
                userCommand = console.readln().trim().split(" ");
                commandStatus = launchCommand(userCommand);
            }

            console.selectConsoleScanner();
            if (commandStatus == ExitCode.OK) {
                console.println("Скрипт успешно выполнен");
            } else if (commandStatus == ExitCode.ERROR) {
                console.println("Проверьте скрипт на корректность введённых данных");
            }
            return commandStatus;
        } catch (FileNotFoundException exception) {
            console.printError("Файл со скриптом не найден");
        } catch (IllegalStateException exception) {
            console.printError("Непредвиденная ошибка");
            System.exit(0);
        } finally {
            console.selectConsoleScanner();
        }

        return ExitCode.ERROR;
    }

    /**
     * Осуществляет запуск команды.
     * @param userCommand команды для запуска
     * @return код завершения
     */
    private ExitCode launchCommand(String[] userCommand) {
        String commandName = userCommand[0];
        Command command = commandManager.getCommandByName(commandName);
        if (Objects.isNull(command)) {
            console.println("Команда '" + commandName + "' не найдена. Наберите 'help' для справки");
            return ExitCode.ERROR;
        }

        String[] args = Arrays.copyOfRange(userCommand, 1, userCommand.length);
        if (command.apply(args)) {
            commandManager.addToHistory(commandName);
            return switch (commandName) {
                case "exit" -> ExitCode.EXIT;
                case "execute_script" -> {
                    ExitCode exitCode = scriptMode(String.join("", args));
                    scriptRecursiveDepth = 0;
                    yield exitCode;
                }
                default -> ExitCode.OK;
            };
        } else {
            return ExitCode.ERROR;
        }
    }
}

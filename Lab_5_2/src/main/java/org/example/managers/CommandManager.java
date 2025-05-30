package org.example.managers;

import org.example.commands.Command;

import java.util.*;

/**
 * Класс, управляющий коллекцией объектов класса {@link Command}.
 */
public class CommandManager {
    private final Map<String, Command> commands = new LinkedHashMap<>();
    private final List<String> commandHistory = new ArrayList<>();

    /**
     * Добавляет команду.
     * @param commandName Название команды
     * @param command Команда
     */
    public void register(String commandName, Command command) {
        commands.put(commandName, command);
    }

    /**
     * @return Словарь команд
     */
    public Map<String, Command> getCommands() {
        return commands;
    }

    /**
     * Отображение из имени команды в объект {@code Command}.
     * @param name искомое имя команды
     * @return null, в случае отсутствия {@code name}, и {@code Command} в ином случае
     */
    public Command getCommandByName(String name) {
        if (Objects.isNull(name)) {
            return null;
        }
        return commands.get(name);
    }

    /**
     * @return История команд
     */
    public List<String> getCommandHistory() {
        return commandHistory;
    }

    /**
     * Добавляет команду в историю.
     * @param command команда
     */
    public void addToHistory(String command) {
        commandHistory.add(command);
    }
}

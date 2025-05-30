package org.example.models;

import org.example.utility.Console;

import java.util.NoSuchElementException;

/**
 * Класс, конструирующий объекты типа {@link LabWork} и его составные части по запросам пользователя в {@link Console}.
 */
public class Ask {

    /**
     * Конструирует {@code LabWork}.
     * @param console консоль для общения с пользователем
     * @param id {@code id}, которым будет обладать {@code LabWork}
     * @return {@code LabWork}, если не произойдет ошибок чтения, и null, иначе
     */
    public static LabWork askLabWork(Console console, Long id) {
        try {
            String name;
            boolean firstMessage = true;
            do {
                if (!firstMessage) {
                    console.println("nameLabWork не может быть равен null и пустой строке");
                }
                console.ask("nameLabWork: ");
                name = console.readln().trim();
                firstMessage = false;
            } while (name.isEmpty());

            Coordinates coordinates = askCoordinates(console);

            Double minimalPoint = null;
            while (true) {
                console.ask("minimalPoint: ");
                String line = console.readln().toUpperCase();
                if (line.isEmpty()) {
                    break;
                }
                try {
                    minimalPoint = Double.parseDouble(line);
                    if (minimalPoint <= 0) {
                        console.println("minimalPoint должен быть положительным");
                        continue;
                    }
                    break;
                } catch (NumberFormatException e) {
                    console.println("Аргумент '" + line + "' не является типом Double");
                }
            }

            var difficulty = askDifficulty(console);

            var discipline = askDiscipline(console);

            return new LabWork(id, name, coordinates, minimalPoint, difficulty, discipline);
        } catch (NoSuchElementException | IllegalStateException e) {
            console.printError("Ошибка чтения");
            return null;
        }
    }

    /**
     * Конструирует {@code Coordinates}.
     * @param console консоль для общения с пользователем
     * @return {@code Coordinates}, если не произойдет ошибок чтения, и null, иначе
     */
    public static Coordinates askCoordinates(Console console) {
        try {
            int[] xy = new int[2];
            for (int i = 0; i < 2; i++) {
                while (true) {
                    String currentCoordinate = i == 0 ? "x" : "y";
                    console.ask("coordinates." + currentCoordinate + ": ");
                    String line = console.readln().trim();
                    if (line.isEmpty()) {
                        console.println(currentCoordinate + " не может быть равен null");
                        continue;
                    }
                    try {
                        xy[i] = Integer.parseInt(line);
                        break;
                    } catch (NumberFormatException e) {
                        console.println("Аргумент '" + line + "' не является типом Integer");
                    }
                }
            }

            return new Coordinates(xy[0], xy[1]);
        } catch (NoSuchElementException | IllegalStateException e) {
            console.printError("Ошибка чтения");
            return null;
        }
    }

    /**
     * Конструирует {@code Difficulty}.
     * @param console консоль для общения с пользователем
     * @return {@code Difficulty}, если не произойдет ошибок чтения, и null, иначе
     */
    public static Difficulty askDifficulty(Console console) {
        try {
            Difficulty difficulty;
            while (true) {
                console.ask("Difficulty (" + Difficulty.names() + "): ");
                String line = console.readln().toUpperCase().trim();
                if (line.isEmpty()) {
                    console.println("Difficulty не может быть равен null");
                    continue;
                }
                try {
                    difficulty = Difficulty.valueOf(line);
                    break;
                } catch (NullPointerException | IllegalArgumentException e) {
                    console.println("Difficulty с таким названием отсутствует");
                }
            }
            return difficulty;
        } catch (NoSuchElementException | IllegalStateException e) {
            console.printError("Ошибка чтения");
            return null;
        }
    }

    /**
     * Конструирует {@code Discipline}.
     * @param console консоль для общения с пользователем
     * @return {@code Discipline}, если не произойдет ошибок чтения, и null, иначе
     */
    public static Discipline askDiscipline(Console console) {
        try {
            console.ask("Discipline (введите пустую строку для значения null, либо что угодно иначе): ");
            if (console.readln().isEmpty()) {
                return null;
            }

            String name;
            boolean firstMessage = true;
            do {
                if (!firstMessage) {
                    console.println("nameDiscipline не может быть равен null и пустой строке");
                }
                console.ask("nameDiscipline: ");
                name = console.readln().trim();
                firstMessage = false;
            } while (name.isEmpty());

            long[] hours = new long[2];
            for (int i = 0; i < 2; i++) {
                String currentHours = i == 0 ? "lectureHours" : "practiceHours";
                while (true) {
                    console.ask(currentHours + ": ");
                    String line = console.readln().trim();
                    if (line.isEmpty()) {
                        console.println(currentHours + " не может быть равен null");
                        continue;
                    }
                    try {
                        if (Long.parseLong(line) < 0) {
                            console.println("Аргумент '" + line + "' не может быть отрицательным");
                        } else {
                            hours[i] = Long.parseLong(line);
                            break;
                        }
                    } catch (NumberFormatException e) {
                        console.println("Аргумент '" + line + "' не является типом Long");
                    }
                }
            }

            return new Discipline(name, hours[0], hours[1]);
        } catch (NoSuchElementException | IllegalStateException e) {
            console.printError("Ошибка чтения");
            return null;
        }
    }
}

# Лабораторная работа #5 - Управление коллекцией LabWork

Вариант: 49044

## Описание программы
Программа представляет собой консольное приложение для управления коллекцией объектов типа `LabWork` с возможностью сохранения/загрузки данных и работы в интерактивном режиме.

## Требования к реализации
- Коллекция хранится в `LinkedHashSet<LabWork>`
- Автоматическая загрузка данных при старте из CSV-файла
- Сохранение данных в файл по команде
- Поддержка интерактивного режима с набором команд
- Обработка ошибок ввода и доступа к файлам

## Формат запуска
```bash
java -jar Lab_5.jar filename_without_extension
```
Где `filename_without_extension` - имя CSV-файла с данными (без расширения)

## Структура классов
### Основной класс:
```java
public class LabWork {
    private long id;                 // Генерируется автоматически (>0, уникальное)
    private String name;             // Не null, не пустая строка
    private Coordinates coordinates; // Не null
    private ZonedDateTime creationDate; // Генерируется автоматически
    private Double minimalPoint;     // Может быть null, >0
    private Difficulty difficulty;   // Не null (enum)
    private Discipline discipline;   // Может быть null
}
```

### Вспомогательные классы:
```java
public class Coordinates {
    private Integer x;  // Не null
    private int y;
}

public class Discipline {
    private String name;  // Не null, не пустая строка
    private long lecturedHours;
    private long practicHours;
}

public enum Difficulty {
    EASY, NORMAL, VERY_HARD, HOPELESS, TERRIBLE
}
```

## Доступные команды
| Команда | Описание |
|---------|----------|
| `help` | Вывести справку по командам |
| `info` | Информация о коллекции |
| `show` | Вывести все элементы |
| `add` | Добавить новый элемент |
| `update id` | Обновить элемент по ID |
| `remove_by_id id` | Удалить элемент по ID |
| `clear` | Очистить коллекцию |
| `save` | Сохранить в файл |
| `execute_script file` | Выполнить скрипт |
| `exit` | Выход без сохранения |
| `add_if_max` | Добавить, если превышает максимальный |
| `remove_lower` | Удалить меньшие элементы |
| `history` | Последние 15 команд |
| `count_by_minimal_point` | Количество элементов с заданным minimalPoint |
| `print_field_ascending_discipline` | Значения discipline по возрастанию |
| `print_field_descending_difficulty` | Значения difficulty по убыванию |

## Формат ввода
- Простые аргументы вводятся в одной строке с командой
- Сложные объекты вводятся по полям (по одному в строке)
- Enum-ы вводятся как имена констант (список выводится)
- `null` вводится пустой строкой
- Автоматические поля не запрашиваются

## Формат данных
Данные хранятся в CSV-файле со следующей структурой:
```
id,name,coordinates_x,coordinates_y,creationDate,minimalPoint,difficulty,discipline_name,lecturedHours,practicHours
```

## Обработка ошибок
Программа корректно обрабатывает:
- Ошибки пользовательского ввода
- Отсутствие прав доступа к файлу
- Некорректные данные в файле
- Нарушения ограничений полей

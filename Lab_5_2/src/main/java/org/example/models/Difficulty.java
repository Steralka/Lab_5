package org.example.models;

/**
 * Перечисление уровней сложности {@link LabWork}.
 */
public enum Difficulty {
    EASY,
    NORMAL,
    VERY_HARD,
    HOPELESS,
    TERRIBLE;

    /**
     * Соединяет все уровни сложности в одну строку.
     * @return итоговая строка
     */
    public static String names() {
        StringBuilder nameList = new StringBuilder();
        for (Difficulty difficulty : values()) {
            nameList.append(difficulty.name()).append(", ");
        }
        nameList.setLength(nameList.length() - 2);
        return nameList.toString();
    }
}

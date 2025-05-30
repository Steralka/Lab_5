package org.example.managers;

import org.example.models.Difficulty;
import org.example.models.Discipline;
import org.example.models.LabWork;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Класс, управляющий коллекцией объектов класса {@link LabWork}.
 */
public class CollectionManager {
    private static final long FIRST_FREE_ID = 1L;
    private long freeId = FIRST_FREE_ID;
    private final Map<Long, LabWork> labWorks = new HashMap<>();
    private final Set<LabWork> collection = new LinkedHashSet<>();
    private LocalDateTime lastInitTime;
    private LocalDateTime lastSaveTime;
    private final CSVParser csvParser;

    public CollectionManager(CSVParser csvParser) {
        this.lastInitTime = null;
        this.lastSaveTime = null;
        this.csvParser = csvParser;
    }

    /**
     * @return последнее время инициализации
     */
    public LocalDateTime getLastInitTime() {
        return lastInitTime;
    }

    /**
     * @return последнее время сохранения
     */
    public LocalDateTime getLastSaveTime() {
        return lastSaveTime;
    }

    /**
     * @return коллекция
     */
    public Set<LabWork> getCollection() {
        return collection;
    }

    /**
     * Отображение из {@code id} в {@code labWork}.
     * @param id интересующий {@code id}
     * @return null, в случае отсутствия {@code id}, и {@code labWork} в ином случае.
     */
    public LabWork getLabWorkById(long id) {
        return labWorks.get(id);
    }

    /**
     * Проверка, содержится ли {@code labWork} в коллекции.
     * @param lab интересующий {@code labWork}
     * @return результат проверки
     */
    public boolean contains(LabWork lab) {
        if (Objects.isNull(lab)) {
            return false;
        }
        return labWorks.containsKey(lab.getId());
    }

    /**
     * Проверка, содержится ли {@code labWork} с определённым {@code id} в коллекции.
     * @param id интересующий {@code id}
     * @return результат проверки
     */
    public boolean contains(Long id) {
        if (Objects.isNull(id)) {
            return false;
        }
        return labWorks.containsKey(id);
    }

    /**
     * @return размер коллекции
     */
    public int size() {
        return collection.size();
    }

    /**
     * Проверка коллекции на пустоту.
     * @return результат проверки
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * @return свободный {@code id}
     */
    public long getFreeId() {
        while (labWorks.containsKey(freeId)) {
            freeId++;
        }
        return freeId;
    }

    /**
     * Добавляет {@code labWork} в коллекцию.
     * @param lab {@code labWork}, который нужно добавить
     * @return произошло ли добавление
     */
    public boolean add(LabWork lab) {
        if (Objects.isNull(lab) || contains(lab)) {
            return false;
        }
        labWorks.put(lab.getId(), lab);
        collection.add(lab);
        sortCollection();
        return true;
    }

    /**
     * Удаляет LabWork по id из коллекции
     * @param id удаляемое id
     * @return произошло ли удаление
     */
    public boolean remove(Long id) {
        if (Objects.isNull(id)) {
            return false;
        }
        LabWork removed = getLabWorkById(id);
        if (Objects.isNull(removed)) {
            return false;
        }
        labWorks.remove(removed.getId());
        collection.remove(removed);
        return true;
    }

    /**
     * Проверяет, является ли {@code labWork} максимальным среди всей коллекции.
     * @param targetLab проверяемый {@code labWork}
     * @return результат проверки
     */
    public boolean isMaxElement(LabWork targetLab) {
        if (Objects.isNull(targetLab)) {
            return false;
        }
        for (LabWork lab : collection) {
            if (targetLab.compareTo(lab) <= 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Удаляет из коллекции все {@code labWork}, меньшие, чем заданный.
     * @param targetLab заданный {@code labWork}
     */
    public void removeLower(LabWork targetLab) {
        if (Objects.isNull(targetLab)) {
            return;
        }
        List<Long> ids = new ArrayList<>();
        for (LabWork lab : collection) {
            if (lab.compareTo(targetLab) < 0) {
                ids.add(lab.getId());
            }
        }
        for (Long id : ids) {
            remove(id);
        }
    }

    /**
     * Считает количество {@code labWork} с заданным {@code minimalPoint} во всей коллекции.
     * @param minimalPoint искомый {@code minimalPoint}
     * @return количество таких элементов
     */
    public int countEqualMinimalPoint(Double minimalPoint) {
        int count = 0;
        for (LabWork lab : collection) {
            if (Objects.equals(lab.getMinimalPoint(), minimalPoint)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Собирает в {@code List} значения поля discipline всех элементов коллекции в порядке возрастания.
     * @return {@code List}, состоящий из {@code Discipline}
     */
    public List<Discipline> getFieldAscendingDiscipline() {
        List<Discipline> result = new ArrayList<>();
        for (LabWork lab : collection) {
            result.add(lab.getDiscipline());
        }
        return result;
    }

    /**
     * Собирает в {@code List} значения поля difficulty всех элементов коллекции в порядке убывания.
     * @return {@code List}, состоящий из {@code Difficulty}
     */
    public List<Difficulty> getFieldDescendingDifficulty() {
        List<Difficulty> result = new ArrayList<>();
        for (LabWork lab : collection) {
            result.add(lab.getDifficulty());
        }
        Collections.reverse(result);
        return result;
    }

    /**
     * Очищает коллекцию.
     */
    public void clear() {
        collection.clear();
        labWorks.clear();
        freeId = FIRST_FREE_ID;
    }

    /**
     * Сортирует элементы коллекции в порядке неубывания.
     */
    public void sortCollection() {
        List<LabWork> list = new ArrayList<>(collection);
        Collections.sort(list);
        collection.clear();
        collection.addAll(list);
    }

    /**
     * Сохраняет коллекцию в файл.
     */
    public void saveCollection() {
        csvParser.writeCollection(collection);
        lastSaveTime = LocalDateTime.now();
    }

    /**
     * Загружает элементы в коллекцию из файла.
     * @return корректность уникальности {@code id} полученных элементов
     */
    public boolean loadCollection() {
        labWorks.clear();
        csvParser.readCSVToCollection(collection);
        lastInitTime = LocalDateTime.now();
        for (LabWork lab : collection) {
            if (labWorks.containsKey(lab.getId())) {
                collection.clear();
                return false;
            } else {
                labWorks.put(lab.getId(), lab);
                freeId = Math.max(freeId, lab.getId() + 1);
            }
        }
        sortCollection();
        return true;
    }

    @Override
    public String toString() {
        StringBuilder info = new StringBuilder();
        for (LabWork lab : collection) {
            info.append(lab.toString()).append(System.lineSeparator());
        }
        return info.substring(0, Math.max(0, info.length() - System.lineSeparator().length()));
    }
}

